package com.nuoze.cctower.rest.applet;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nuoze.cctower.pojo.dto.WxPayDTO;
import com.nuoze.cctower.service.applet.WxOrderService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author JiaShun
 * @date 2019-01-22 23:48
 * 微信支付
 */
@Slf4j
@Controller
@RequestMapping("applet/wx-pay")
public class WxPayController {

    @Autowired
    private WxOrderService wxOrderService;
    @Autowired
    private WxPayService wxPayService;

    /**
     * 微信支付
     *
     * @param dto
     * @param request
     * @return WX付款订单结果
     */
    @PostMapping("prepay")
    @ResponseBody
    public WxPayMpOrderResult wxPrePay(@RequestBody WxPayDTO dto, HttpServletRequest request) {
        log.info("[WX PAY CONTROLLER] Applet submit payment: {}", dto);
        return wxOrderService.wxPrePay(dto, request);
    }

    /**
     * 支付通知
     * WxPayOrderNotifyResult：微信支付订单结果通知
     * orderSn：订单编号
     * payId：付款id
     *
     * @param request
     * @param response
     */
    @PostMapping("pay-notify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            String orderSn = result.getOutTradeNo();
            String payId = result.getTransactionId();
            log.info("[WX PAY CONTROLLER] receive weChat pay orderSn: {}, payId: {}", orderSn, payId);
            wxOrderService.payNotify(result, response);
            String xml = "<xml>\n  <return_code><![CDATA[SUCCESS]]></return_code>\n  <return_msg><![CDATA[OK]]></return_msg>\n</xml>";
            wxpayCallbackResult(response, xml);
        } catch (Exception e) {
            log.error("pay notify has exception: {}", e.getMessage());
        }
    }

    /**
     * 微信支付回调
     *
     * @param response
     * @param xml
     * @throws IOException
     */
    private void wxpayCallbackResult(HttpServletResponse response, String xml) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/xml; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(xml);
        out.close();
    }

}
