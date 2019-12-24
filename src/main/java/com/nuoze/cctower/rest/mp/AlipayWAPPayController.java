package com.nuoze.cctower.rest.mp;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.nuoze.cctower.common.enums.IncomeType;
import com.nuoze.cctower.component.BillingComponent;
import com.nuoze.cctower.component.MqSendComponent;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.config.AlipayProperties;
import com.nuoze.cctower.dao.CarDAO;
import com.nuoze.cctower.dao.OrderNumberDAO;
import com.nuoze.cctower.dao.PassagewayDAO;
import com.nuoze.cctower.pojo.dto.AlipayDTO;
import com.nuoze.cctower.pojo.entity.*;
import com.nuoze.cctower.pojo.vo.GoOutVO;
import com.nuoze.cctower.service.AccountService;
import com.nuoze.cctower.service.ParkingRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import static com.nuoze.cctower.common.constant.Constant.*;
import static com.nuoze.cctower.common.constant.Constant.BUSINESS_NORMAL_CAR;

/**
 * 支付宝-手机网站支付.
 * <p>
 * 手机网站支付
 *
 * @author Mengday Zhang
 * @version 1.0
 * @since 2018/6/11
 */
@Slf4j
@Controller
@RequestMapping("/alipay/wap")
public class AlipayWAPPayController {

    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private ParkingRecordService parkingRecordService;
    @Autowired
    private OrderNumberDAO orderNumberDAO;

    /**
     * 去支付
     * <p>
     * 支付宝返回一个form表单，并自动提交，跳转到支付宝页面
     *  https://docs.open.alipay.com/203/107090/
     *  必填参数：
     *      subject：商品标题
     *      out_trade_no：唯一订单号
     *      total_amount：订单总金额
     *      product_code：销售产品码，商家和支付宝签约的产品码。该产品请填写固定值
     *  选填参数：
     *      body：商品描述信息
     *      timeout_express：该笔订单允许的最晚付款时间，逾期将关闭交易
     * @param response
     * @throws Exception
     */
    @PostMapping("/gotoPayPage")
    public void gotoPayPage(@RequestBody AlipayDTO dto, HttpServletResponse response) throws AlipayApiException, IOException {
        // 订单模型
        String productCode = "QUICK_WAP_WAY";
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(UUID.randomUUID().toString());
        model.setSubject("停车费");
        model.setTotalAmount(dto.getMoney());
        model.setBody("CCTower停车费，共" + dto.getMoney() +"元");
        model.setTimeoutExpress("5m");
        model.setProductCode(productCode);

        AlipayTradeWapPayRequest wapPayRequest = new AlipayTradeWapPayRequest();
        wapPayRequest.setReturnUrl("http://yxep7y.natappfree.cc/alipay/wap/returnUrl");
        wapPayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());
        wapPayRequest.setBizModel(model);

        // 调用SDK生成表单, 并直接将完整的表单html输出到页面
        String form = alipayClient.pageExecute(wapPayRequest).getBody();
        System.out.println(form);
        response.setContentType("text/html;charset=" + alipayProperties.getCharset());
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
        //更新订单信息
        ParkingRecord parkingRecord = parkingRecordService.findById(dto.getRecordId());
        if (StringUtils.isEmpty(parkingRecord.getAppletOrderSn())) {
            parkingRecord.setAlPayOrderSn(model.getOutTradeNo());
        } else {
            orderNumberDAO.insert(new OrderNumber().setParkingRecordId(parkingRecord.getId()).setOrderSn(model.getOutTradeNo()).setCreateTime(new Date()));
        }
        parkingRecordService.update(parkingRecord);
    }


    /**
     * 支付宝页面跳转同步通知页面
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    @RequestMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
        response.setContentType("text/html;charset=" + alipayProperties.getCharset());

        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean verifyResult = AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipayPublicKey(), alipayProperties.getCharset(), "RSA2");
        if (verifyResult) {
            //验证成功
            //请在这里加上商户的业务逻辑程序代码，如保存支付宝交易号
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //交易金额
            BigDecimal money = BigDecimal.valueOf(Integer.valueOf(new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8")));

            return "redirect:/mp/watchPay/forward";
        } else {
            return "wapPayFail";

        }
    }
}
