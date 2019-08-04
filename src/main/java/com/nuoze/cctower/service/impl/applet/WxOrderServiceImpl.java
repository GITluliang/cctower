package com.nuoze.cctower.service.impl.applet;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nuoze.cctower.common.enums.IncomeType;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.common.util.WxUtils;
import com.nuoze.cctower.component.MqSendComponent;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.dao.*;
import com.nuoze.cctower.pojo.dto.WxPayDTO;
import com.nuoze.cctower.pojo.entity.*;
import com.nuoze.cctower.pojo.vo.GoOutVO;
import com.nuoze.cctower.service.ParkingRecordService;
import com.nuoze.cctower.service.applet.MemberService;
import com.nuoze.cctower.service.applet.TopUpRecordService;
import com.nuoze.cctower.service.applet.WxOrderService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.nuoze.cctower.common.constant.Constant.*;

/**
 * @author JiaShun
 * @date 2019-04-07 18:28
 */
@Slf4j
@Service
public class WxOrderServiceImpl implements WxOrderService {

    @Autowired
    private WxUtils wxUtils;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private ParkingRecordService parkingRecordService;
    @Autowired
    private TopUpRecordService topUpRecordService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MqSendComponent mqSendComponent;
    @Autowired
    private PassagewayDAO passagewayDAO;
    @Autowired
    private CarDAO carDAO;
    @Autowired
    private RenewRecordDAO renewRecordDAO;
    @Autowired
    private PaymentComponent paymentComponent;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private ParkingTradingRecordDAO tradingRecordDAO;

    @Override
    public WxPayMpOrderResult wxPrePay(WxPayDTO dto, HttpServletRequest request) {
        String openId = wxUtils.findOpenIdByCode(dto.getCode());
        BigDecimal actualPrice = new BigDecimal(dto.getMoney());
        WxPayUnifiedOrderRequest orderRequest = paymentComponent.buildWxPayReq(openId, actualPrice, request, null);
        orderRequest.setBody("停车费");
        WxPayMpOrderResult result = null;
        try {
            result = wxPayService.createOrder(orderRequest);
            String prepayId = result.getPackageValue();
            prepayId = prepayId.replace("prepay_id=", "");
            ParkingRecord parkingRecord = parkingRecordService.findById(dto.getRecordId());
            parkingRecord.setOrderSn(orderRequest.getOutTradeNo());
            parkingRecord.setPrepayId(prepayId);
            parkingRecord.setOpenId(openId);
            parkingRecordService.update(parkingRecord);
        } catch (WxPayException e) {
            log.error("pre pay has exception: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public void payNotify(WxPayOrderNotifyResult result, HttpServletResponse response) {
        try {
            String orderSn = result.getOutTradeNo();
            String payId = result.getTransactionId();
            BigDecimal money = new BigDecimal(BaseWxPayResult.fenToYuan(result.getTotalFee()));
            ParkingRecord parkingRecord = parkingRecordService.findByOrderSn(orderSn);
            TopUpRecord topUpRecord = topUpRecordService.findByOrderSn(orderSn);
            RenewRecord renewRecord = renewRecordDAO.findByOrderSn(orderSn);
            if (parkingRecord == null && topUpRecord == null && renewRecord == null) {
                log.error("[PAY NOTIFY PARKING-RECORD] 订单不存在 orderSn: {}", orderSn);
            }
            if (parkingRecord != null) {
                log.info("[PAY NOTIFY PARKING-RECORD] id: {}, pay_id: {}", parkingRecord.getId(), result.getTransactionId());
                parkingRecord.setCost(money);
                parkingRecord.setPayId(payId);
                parkingRecord.setPayType(PAYMENT_WECHAT);
                parkingRecord.setPayTime(new Date());
                parkingRecord.setPayStatus(1);
                int status = parkingRecord.getStatus();
                Long parkingId = parkingRecord.getParkingId();
                Car car = carDAO.findByParkingIdAndCarNumber(parkingId, parkingRecord.getCarNumber());
                //如果是待出门，通过mq发送出门指令
                if (status == READY_TO_LEAVE) {
                    GoOutVO goOutVO = new GoOutVO();
                    goOutVO.setParkingId(parkingId);
                    if (parkingRecord.getExitId() != null) {
                        Passageway passageway = passagewayDAO.selectByPrimaryKey(parkingRecord.getExitId());
                        if (passageway != null) {
                            String ip = passageway.getIp();
                            if (StringUtils.isNotBlank(ip)) {
                                goOutVO.setIp(ip);
                            }
                        }
                    }
                    goOutVO.setCarNumber(parkingRecord.getCarNumber());
                    if (car != null) {
                        goOutVO.setType(car.getParkingType());
                    } else {
                        goOutVO.setType(0);
                    }
                    mqSendComponent.sendGoOutCar(parkingId, goOutVO);
                    parkingRecord.setStatus(LEAVE_YET);
                }
                if (car != null && BUSINESS_CAR == car.getParkingType() && BUSINESS_NORMAL_CAR == car.getStatus()) {
                    car.setStatus(BUSINESS_FORBIDDEN_CAR);
                    car.setUpdateTime(new Date());
                    carDAO.updateByPrimaryKeySelective(car);
                }
                addTradingRecord(money, parkingId, IncomeType.PARKING_CHARGE);
                addAccountBalance(money, parkingId);
                parkingRecordService.update(parkingRecord);
            }
            if (topUpRecord != null) {
                log.info("[PAY NOTIFY TOP-UP-RECORD] id: {}, pay_id: {}", topUpRecord.getId(), topUpRecord.getPayId());
                topUpRecord.setPayId(payId);
                topUpRecord.setPayStatus(1);
                topUpRecord.setUpdateTime(new Date());
                topUpRecordService.update(topUpRecord);
                String openId = topUpRecord.getOpenId();
                Member member = memberService.findByOpenId(openId);
                member.setBalance(topUpRecord.getBalance());
                memberService.update(member);
            }
            if (renewRecord != null) {
                log.info("[PAY NOTIFY RENEW-RECORD] id: {}, pay_id: {}", renewRecord.getId(), renewRecord.getPayId());
                renewRecord.setPayId(payId);
                renewRecord.setPayStatus(1);
                renewRecordDAO.updateByPrimaryKeySelective(renewRecord);
                int monthCount = renewRecord.getMonthCount();
                Car car = carDAO.findByCarNumberAndParkingType(renewRecord.getCarNumber(), MONTHLY_CAR);
                Date monthlyParkingEnd = DateUtils.addMonthByDate(car.getMonthlyParkingEnd(), monthCount);
                car.setMonthlyParkingEnd(monthlyParkingEnd);
                car.setUpdateTime(new Date());
                carDAO.updateByPrimaryKeySelective(car);
                addTradingRecord(money, renewRecord.getParkingId(), IncomeType.RENT_RENEW);
                addAccountBalance(money, renewRecord.getParkingId());
            }
        } catch (Exception e) {
            log.error("pay notify has exception: {}", e.getMessage());
        }
    }

    /**
     * 增加交易流水
     * @param money 流水金额
     * @param parkingId 停车场ID
     * @param type 收入类型
     */
    private void addTradingRecord(BigDecimal money, Long parkingId, IncomeType type) {
        ParkingTradingRecord tradingRecord = new ParkingTradingRecord();
        tradingRecord.setAmount(money);
        tradingRecord.setType(PARKING_TRADING_RECORD_INCOME_TYPE);
        tradingRecord.setParkingId(parkingId);
        tradingRecord.setPayTime(new Date());
        tradingRecord.setIncomeType(type.name());
        tradingRecordDAO.insert(tradingRecord);

    }

    /**
     * 更新停车场余额
     * @param money 金额
     * @param parkingId 停车场ID
     */
    private void addAccountBalance(BigDecimal money, Long parkingId) {
        Account account = accountDAO.selectByParkingId(parkingId);
        account.setBalance(account.getBalance().add(money));
        accountDAO.updateByPrimaryKeySelective(account);
    }
}
