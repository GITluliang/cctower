package com.nuoze.cctower.service.applet;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.pojo.dto.WxPayDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JiaShun
 * @date 2019-04-07 18:27
 */
public interface WxOrderService {
    WxPayMpOrderResult wxPrePay(WxPayDTO dto, HttpServletRequest request);

    void payNotify(WxPayOrderNotifyResult result, HttpServletResponse response);
}
