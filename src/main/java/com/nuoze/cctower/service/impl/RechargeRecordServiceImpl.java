package com.nuoze.cctower.service.impl;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.dao.RechargeRecordDAO;
import com.nuoze.cctower.pojo.dto.RechargeDTO;
import com.nuoze.cctower.pojo.entity.RechargeRecord;
import com.nuoze.cctower.pojo.entity.RenewRecord;
import com.nuoze.cctower.pojo.vo.RechargeRecordVO;
import com.nuoze.cctower.service.RechargeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author luliang
 * @Date 2019-12-04 12:03
 */
@Slf4j
@Service
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private PaymentComponent paymentComponent;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private RechargeRecordDAO rechargeRecordDAO;

    @Override
    public WxPayMpOrderResult businessrRechargePrePay(RechargeDTO dto, HttpServletRequest request) {
        BigDecimal actualPrice = new BigDecimal(dto.getCost());
        WxPayUnifiedOrderRequest orderRequest = paymentComponent.buildWxPayReq(dto.getOpenId(), actualPrice, request, null);
        orderRequest.setBody("商户充值");
        orderRequest.setAppid("wxc1487ff13e8c64df");
        WxPayMpOrderResult result = null;
        try {
            result = wxPayService.createOrder(orderRequest);
            String prepayId = result.getPackageValue();
            prepayId = prepayId.replace("prepay_id=", "");
            rechargeRecordDAO.insertSelective(new RechargeRecord().setOpenId(dto.getOpenId()).setOrderSn(orderRequest.getOutTradeNo()).setPrepayId(prepayId).setUserId(dto.getUserId()).setCost(actualPrice).setPayStatus(0).setCreateTime(new Date()));
        } catch (WxPayException e) {
            log.error("[RENEW CAR ] pre pay has exception: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public List<RechargeRecordVO> list(Map<String, Object> params) {
        CopyOnWriteArrayList<RechargeRecordVO> recordVOS = new CopyOnWriteArrayList<>();
        List<RechargeRecord> records = rechargeRecordDAO.list(params);
        for (RechargeRecord record : records) {
            RechargeRecordVO recordVO = new RechargeRecordVO();
            BeanUtils.copyProperties(record, recordVO);
            recordVO.setCreateTimeStr(DateUtils.formatDateTime(record.getCreateTime()));
            recordVOS.add(recordVO);
        }
        return recordVOS;
    }
}
