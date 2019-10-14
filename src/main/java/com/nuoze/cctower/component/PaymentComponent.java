package com.nuoze.cctower.component;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.nuoze.cctower.common.util.IpUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.nuoze.cctower.common.constant.Constant.MINIMUM_SERVICE_CHARGE;
import static com.nuoze.cctower.common.constant.Constant.ONE_HUNDRED;


/**
 * @author JiaShun
 * @date 2019-05-14 00:01
 */
@Component
public class PaymentComponent {

    public Map<String, Object> paymentRecord(JSONObject json) {
        String openId = json.getString("openId");
        Integer page = json.getInteger("pageNum");
        Integer limit = json.getInteger("limit");
        if (StringUtils.isEmpty(openId)) {
            return null;
        }
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 20;
        }
        Integer offset = (page - 1) * limit;
        Map<String, Object> map = new HashMap<>(4);
        map.put("openId", openId);
        map.put("offset", offset);
        map.put("limit", limit);
        return map;
    }

    /**
     * 构建微信支付请求
     * @param openId 小程序Id
     * @param actualPrice 实际价格
     * @param request HttpServletRequest请求
     * @param ip IP
     * @return WxPayUnifiedOrderRequest
     */
    public WxPayUnifiedOrderRequest buildWxPayReq(String openId, BigDecimal actualPrice, HttpServletRequest request, String ip) {
        String orderSn = UUID.randomUUID().toString().replace("-", "");
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setOutTradeNo(orderSn);
        if (!StringUtils.isEmpty(openId)) {
            orderRequest.setOpenid(openId);
        }
        //将元转换为分，微信的api只接收分
        int fee = actualPrice.multiply(new BigDecimal(100)).intValue();
        orderRequest.setTotalFee(fee);
        if (ip == null) {
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));
        } else {
            orderRequest.setSpbillCreateIp(ip);
        }
        return orderRequest;
    }

    public BigDecimal balanceToWithdrawalAmount(BigDecimal balance, Integer serviceCharge) {
        return balance.multiply(dividedOneHundred(ONE_HUNDRED - serviceCharge)).setScale(2, 1);
    }

    /**
     * 微信折扣
     * @param cost 车费
     * @param serviceCharge 折扣
     * @return
     */
    public BigDecimal wechatDiscounted(BigDecimal cost, Integer serviceCharge) {
        return cost.multiply(dividedOneHundred(serviceCharge)).setScale(2, 1);
    }

    public BigDecimal getServiceCharge(BigDecimal amount, Integer serviceCharge) {
        BigDecimal serviceChargeCost = amount.multiply(dividedOneHundred(serviceCharge)).setScale(2, 1);
        if (serviceChargeCost.compareTo(MINIMUM_SERVICE_CHARGE) <= 0) {
            serviceChargeCost = MINIMUM_SERVICE_CHARGE;
        }
        return serviceChargeCost;
    }
    public BigDecimal getBalanceByAmountAndServiceCharge(BigDecimal balance, BigDecimal amount, BigDecimal serviceCharge) {
        return balance.subtract(amount.add(serviceCharge)).setScale(2, 1);
    }

    private BigDecimal dividedOneHundred(Integer num) {
        return BigDecimal.valueOf((double) num / 100.0D).setScale(2, 1);
    }
}
