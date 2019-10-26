package com.nuoze.cctower.service.applet;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.pojo.dto.WxPayDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信订单
 *
 * @author JiaShun
 * @date 2019-04-07 18:27
 */
public interface WxOrderService {
    /**
     * WX付款订单结果(小程序)
     *
     * @param dto
     * @param request
     * @return
     */
    WxPayMpOrderResult wxPrePay(WxPayDTO dto, HttpServletRequest request);

    /**
     * WX付款订单结果(H5公众号)
     * @param dto
     * @param request
     * @return
     */
    WxPayMpOrderResult wxPrePayH5(WxPayDTO dto, HttpServletRequest request);

    /**
     * 支付通知
     *
     * @param result
     * @param response
     */
    void payNotify(WxPayOrderNotifyResult result, HttpServletResponse response);
}
