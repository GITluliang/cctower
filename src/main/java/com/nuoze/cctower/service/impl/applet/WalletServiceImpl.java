package com.nuoze.cctower.service.impl.applet;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.common.util.IpUtil;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.pojo.dto.WalletDTO;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.pojo.entity.TopUpRecord;
import com.nuoze.cctower.pojo.vo.TopUpRecordVO;
import com.nuoze.cctower.service.ParkingService;
import com.nuoze.cctower.service.applet.MemberService;
import com.nuoze.cctower.service.applet.TopUpRecordService;
import com.nuoze.cctower.service.applet.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JiaShun
 * @date 2019-01-19 00:49
 */
@Slf4j
@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private TopUpRecordService topUpRecordService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private PaymentComponent paymentComponent;

    @Override
    public WxPayMpOrderResult walletPrepay(WalletDTO dto, HttpServletRequest request) {
        String openId = dto.getOpenId();
        BigDecimal actualPrice = new BigDecimal(dto.getAmount());
        WxPayUnifiedOrderRequest orderRequest = paymentComponent.buildWxPayReq(openId, actualPrice, request, null);
        orderRequest.setBody("充值");
        WxPayMpOrderResult result = null;
        try {
            result = wxPayService.createOrder(orderRequest);
            String prepayId = result.getPackageValue();
            prepayId = prepayId.replace("prepay_id=", "");
            TopUpRecord topUpRecord = new TopUpRecord();
            topUpRecord.setAmount(actualPrice);
            topUpRecord.setBillingType(1);
            topUpRecord.setOpenId(openId);
            topUpRecord.setOrderSn(orderRequest.getOutTradeNo());
            topUpRecord.setPrepayId(prepayId);
            topUpRecord.setPayStatus(0);
            Member member = memberService.findByOpenId(openId);
            BigDecimal balance = member.getBalance().add(actualPrice);
            topUpRecord.setBalance(balance);
            topUpRecordService.save(topUpRecord);
        } catch (WxPayException e) {
            log.error("pre pay has exception: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public List<TopUpRecordVO> recordList(Map<String, Object> map) {
        List<TopUpRecord> list = topUpRecordService.findByOpenId(map);
        List<TopUpRecordVO> topUpRecordVOS = null;
        if (!CollectionUtils.isEmpty(list)) {
            topUpRecordVOS = new CopyOnWriteArrayList<>();
            for (TopUpRecord top : list) {
                TopUpRecordVO vo = new TopUpRecordVO();
                int billingType = top.getBillingType();
                if (0 == billingType) {
                    Long parkingId = top.getParkingId();
                    String parkingName = parkingService.findById(parkingId).getName();
                    vo.setType("停车费: " + parkingName);
                    vo.setAmount("- " + top.getAmount().toString());
                } else if (1 == billingType) {
                    vo.setType("充值:");
                    vo.setAmount("+ " + top.getAmount().toString());
                }
                vo.setTime(DateUtils.formatDateTime(top.getCreateTime()));
                topUpRecordVOS.add(vo);
            }
        }
        return topUpRecordVOS;
    }
}
