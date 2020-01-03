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
import com.nuoze.cctower.component.BillingComponent;
import com.nuoze.cctower.component.MqSendComponent;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.dao.*;
import com.nuoze.cctower.pojo.dto.WxPayDTO;
import com.nuoze.cctower.pojo.entity.*;
import com.nuoze.cctower.pojo.vo.GoOutVO;
import com.nuoze.cctower.service.AccountService;
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
import java.util.Map;

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
    private BillingComponent billingComponent;
    @Autowired
    private OrderNumberDAO orderNumberDAO;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RechargeRecordDAO rechargeRecordDAO;
    @Autowired
    private BusinessTransactionRecordDAO businessTransactionRecordDAO;
    @Autowired
    private UserDAO userDAO;

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
            //*** 保存小程序支付订单号,如果是第一次保存在parkingRecord中，如果是第二次保存在OrderNumber中 ***
            if (StringUtils.isEmpty(parkingRecord.getAppletOrderSn())) {
                parkingRecord.setAppletOrderSn(orderRequest.getOutTradeNo());
            } else {
                orderNumberDAO.insert(new OrderNumber().setParkingRecordId(parkingRecord.getId()).setOrderSn(orderRequest.getOutTradeNo()).setCreateTime(new Date()));
            };
            parkingRecordService.update(parkingRecord.setPrepayId(prepayId).setOpenId(openId));
        } catch (WxPayException e) {
            log.error("pre pay has exception: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public WxPayMpOrderResult wxPrePayH5(WxPayDTO dto, HttpServletRequest request) {
        BigDecimal actualPrice = new BigDecimal(dto.getMoney());
        WxPayUnifiedOrderRequest orderRequest = paymentComponent.buildWxPayReq(dto.getOpenId(), actualPrice, request, null);
        orderRequest.setBody("停车费");
        orderRequest.setAppid("wxc1487ff13e8c64df");
        WxPayMpOrderResult result = null;
        try {
            result = wxPayService.createOrder(orderRequest);
            String prepayId = result.getPackageValue();
            prepayId = prepayId.replace("prepay_id=", "");
            ParkingRecord parkingRecord = parkingRecordService.findById(dto.getRecordId());
            //*** 保存小程序支付订单号,如果是第一次保存在parkingRecord中，如果是第二次保存在OrderNumber中 ***
            if (StringUtils.isEmpty(parkingRecord.getAppletOrderSn())) {
                parkingRecord.setAppletOrderSn(orderRequest.getOutTradeNo());
            } else {
                orderNumberDAO.insert(new OrderNumber().setParkingRecordId(parkingRecord.getId()).setOrderSn(orderRequest.getOutTradeNo()).setCreateTime(new Date()));
            }
            parkingRecordService.update( parkingRecord.setPrepayId(prepayId).setOpenId(dto.getOpenId()));
        } catch (WxPayException e) {
            log.error("pre pay has exception: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public void payNotify(WxPayOrderNotifyResult result, HttpServletResponse response) {
        try {
            //orderSn：订单号、payId：交易id、money：支付费用
            String orderSn = result.getOutTradeNo();
            String payId = result.getTransactionId();
            BigDecimal money = new BigDecimal(BaseWxPayResult.fenToYuan(result.getTotalFee()));
            //根据订单，查询停车记录
            ParkingRecord parkingRecord = parkingRecordService.findByOrderSn(orderSn);
            if (parkingRecord == null) {
                OrderNumber orderNumber = orderNumberDAO.findByorderSn(orderSn);
                if (orderNumber != null) {
                    parkingRecord = parkingRecordService.findById(orderNumber.getParkingRecordId());
                }
            }
            TopUpRecord topUpRecord = topUpRecordService.findByOrderSn(orderSn);
            RenewRecord renewRecord = renewRecordDAO.findByOrderSn(orderSn);
            RechargeRecord rechargeRecord = rechargeRecordDAO.findByOrderSn(orderSn);
            if (parkingRecord == null && topUpRecord == null && renewRecord == null && rechargeRecord == null) {
                log.error("[PAY NOTIFY PARKING-RECORD] 订单不存在 orderSn: {}", orderSn);
            }
            BigDecimal serviceCharge = new BigDecimal(0);
            if (parkingRecord != null) {
                log.info("[PAY NOTIFY PARKING-RECORD] id: {}, pay_id: {}", parkingRecord.getId(), result.getTransactionId());
                int status = parkingRecord.getStatus();
                Long parkingId = parkingRecord.getParkingId();
                Car car = carDAO.findByParkingIdAndCarNumber(parkingId, parkingRecord.getCarNumber());
                Account account = accountService.findByParkingId(parkingId);
                if (account != null) {
                    serviceCharge = paymentComponent.getServiceCharge(money, account.getServiceCharge());
                }
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
                    //通过mq发送出厂消息
                    mqSendComponent.sendGoOutCar(parkingId, goOutVO);
                    parkingRecord.setStatus(LEAVE_YET);
                }
                //商户车辆状态修改
                if (car != null) {
                    if (BUSINESS_CAR == car.getParkingType() || TIMECOUPON_CAR == car.getParkingType()) {carDAO.deleteByPrimaryKey(car.getId());}
                }
                parkingRecordService.update(parkingRecord.setServiceCharge(serviceCharge).setOrderSn(orderSn).setCost(money).setPayId(payId).setPayType(PAYMENT_WECHAT).setPayTime(new Date()).setPayStatus(PAY_STATUS_NORMAL).setPayTime(new Date()));
                billingComponent.addTradingRecord(money.subtract(serviceCharge), parkingId, IncomeType.PARKING_CHARGE, parkingRecord.getCarNumber(),serviceCharge);
                billingComponent.addAccountBalance(money.subtract(serviceCharge), parkingId, serviceCharge);
            }
            //小程序长租续费
            if (renewRecord != null) {
                log.info("[PAY NOTIFY Long rent renewals] id: {}, pay_id: {}", renewRecord.getId(), payId);

                Account account = accountService.findByParkingId(renewRecord.getParkingId());
                if (account != null) {
                    serviceCharge = paymentComponent.getServiceCharge(money, account.getServiceCharge());
                }
                Car car = carDAO.findByCarNumberAndParkingType(renewRecord.getCarNumber(), MONTHLY_CAR);
                carDAO.updateByPrimaryKeySelective(car.setMonthlyParkingEnd(DateUtils.addMonthByDate(car.getMonthlyParkingEnd(), renewRecord.getMonthCount())).setUpdateTime(new Date()));
                renewRecordDAO.updateByPrimaryKeySelective(renewRecord.setServiceCharge(serviceCharge).setCost(money).setPayId(payId).setPayStatus(PAY_STATUS_NORMAL));
                billingComponent.addTradingRecord(money.subtract(serviceCharge), renewRecord.getParkingId(), IncomeType.RENT_RENEW, renewRecord.getCarNumber(),serviceCharge);
                billingComponent.addAccountBalance(money.subtract(serviceCharge), renewRecord.getParkingId(),serviceCharge);
            }
            //小程序余额充值
            if (topUpRecord != null) {
                log.info("[PAY NOTIFY Recharge of balance] id: {}, pay_id: {}", topUpRecord.getId(), payId);
                topUpRecordService.update(topUpRecord.setPayId(payId).setPayStatus(PAY_STATUS_NORMAL).setUpdateTime(new Date()));
                memberService.update(memberService.findByOpenId(topUpRecord.getOpenId()).setBalance(topUpRecord.getBalance()));
            }
            //商户充值
            if(rechargeRecord != null) {
                log.info("[PAY NOTIFY Merchant recharge] id: {}, pay_id: {}", rechargeRecord.getId(), payId);
                Account account = accountService.findByParkingId(rechargeRecord.getParkingId());
                if (account != null) {
                    serviceCharge = paymentComponent.getServiceCharge(money, account.getServiceCharge());
                }
                User user = userDAO.selectByPrimaryKey(rechargeRecord.getUserId());
                //1.更新商户账户余额；2.更新商户微信充值记录；3.增加商户流水；4.更新物业账号流水；5.更新物业账号余额
                userDAO.updateByPrimaryKey(user.setBalance(user.getBalance().add(money)).setUpdateTime(new Date()));
                rechargeRecordDAO.updateByPrimaryKey(rechargeRecord.setServiceCharge(serviceCharge).setPayId(payId).setPayStatus(PAY_STATUS_NORMAL).setCreateTime(new Date()));
                businessTransactionRecordDAO.insert(new BusinessTransactionRecord().setUserId(rechargeRecord.getUserId()).setType(3).setAmount(money).setBalance(user.getBalance()).setCreateTime(new Date()).setStatus(0).setParkingId(rechargeRecord.getParkingId()).setCarNumber(""));
                billingComponent.addTradingRecord(money.subtract(serviceCharge), rechargeRecord.getParkingId(), IncomeType.BUSINESS_RENEW, "",serviceCharge);
                billingComponent.addAccountBalance(money.subtract(serviceCharge), rechargeRecord.getParkingId(),serviceCharge);
            }
        } catch (Exception e) {
            log.error("pay notify has exception: {}", e.getMessage());
        }
    }

}
