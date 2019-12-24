package com.nuoze.cctower.rest.mp;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.pojo.dto.WxPayDTO;
import com.nuoze.cctower.service.applet.WxOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * @author luliang
 * @date 2019-10-26 23:48
 * 微信JSAPI支付
 */
@Slf4j
@Controller
@RequestMapping("mp/wx-pay")
public class MpPayController {

    @Autowired
    private WxOrderService wxOrderService;

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
        log.info("[公众号支付 WX PAY CONTROLLER] Applet submit payment: {}", dto);
        String openId = dto.getOpenId();
        String money = dto.getMoney();
        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(money)) {
            return WxPayMpOrderResult.builder().build();
        }
        return wxOrderService.wxPrePayH5(dto, request);
    }


}
