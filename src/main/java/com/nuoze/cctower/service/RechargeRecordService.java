package com.nuoze.cctower.service;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.pojo.dto.RechargeDTO;
import com.nuoze.cctower.pojo.entity.RechargeRecord;
import com.nuoze.cctower.pojo.vo.RechargeRecordVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author luliang
 * @Date 2019-12-04 12:03
 */
public interface RechargeRecordService {

    /**
     * 商户充值
     *
     * @param dto
     * @param request
     * @return
     */
    WxPayMpOrderResult businessrRechargePrePay(RechargeDTO dto, HttpServletRequest request);

    List<RechargeRecordVO> list(Map<String, Object> params) ;
}
