package com.nuoze.cctower.rest.mobile;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.WxUtils;
import com.nuoze.cctower.pojo.dto.RechargeDTO;
import com.nuoze.cctower.service.RechargeRecordService;
import com.nuoze.cctower.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.nuoze.cctower.common.util.ShiroUtils.*;

/**
 * @Author luliang
 * @Date 2019-12-03 10:29
 * 时长劵商户充值
 */
@Slf4j
@Controller
public class RechargeController {
    private String prefix = "h5/mobile/recharge/";

    @Autowired
    private WxUtils wxUtils;
    @Autowired
    private RechargeRecordService recordService;
    @Autowired
    private UserService userService;

    @RequestMapping("/mobile/recharge")
    public String listRecharge() {
        return prefix + "listRecharge";
    }

    @RequestMapping("/mobile/recharge/state")
    public String state() {
        return prefix + "state";
    }

    @ResponseBody
    @RequestMapping("/mobile/recharge/authz")
    public Result carBusinessAuthZ() {
        return getSubject().isPermitted("sys:car:business") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
    }

    /**
     * 充值记录
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping("/mobile/recharge/rechargeStream")
    public Result rechargeStream(@RequestParam Map<String, Object> params) {
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("user", getUser());
        params.put("userId", getUserId());
        map.put("record",recordService.list(new Query(params)));
        return ResponseResult.success(map);
    }

    /**
     * 去充值
     * @return
     */
    @RequestMapping("/mp/recharge/addRecharge")
    public String addRecharge(String code,  Model model) {
        if (StringUtils.isEmpty(code)) {
            return prefix + "code";
        }
        if (StringUtils.isNotEmpty(code)) {
            model.addAttribute("openId", wxUtils.getUserInfoAccessToken(code).get("openid"));
        }
        model.addAttribute("userVO", userService.findByIdVO(getUserId()));
        return prefix + "addRecharge";
    }

    /**
     * 商户充值，生成支付信息
     * @param dto
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/mp/recharge/prepay")
    public WxPayMpOrderResult businessrRechargePrePay(@RequestBody RechargeDTO dto, HttpServletRequest request) {
        log.info("[商户充值 WX PAY businessrRechargePrePay] Applet submit payment: {}", dto);
        String openId = dto.getOpenId();
        String cost = dto.getCost();
        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(cost)) {
            return WxPayMpOrderResult.builder().build();
        }
        return recordService.businessrRechargePrePay(dto, request);
    }

}
