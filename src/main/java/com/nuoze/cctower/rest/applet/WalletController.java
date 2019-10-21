package com.nuoze.cctower.rest.applet;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.pojo.dto.WalletDTO;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.service.applet.MemberService;
import com.nuoze.cctower.service.applet.WalletService;
import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static com.nuoze.cctower.common.result.ResultEnum.INVALID_PARAM;
import static com.nuoze.cctower.common.result.ResultEnum.PERMISSION_DENIED;

/**
 * @author JiaShun
 * @date 2019-01-15 23:21
 * 微信钱包
 */
@RestController
@RequestMapping("applet/wallet")
public class WalletController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private PaymentComponent paymentComponent;

    @GetMapping("balance")
    public Result findBalance(@RequestParam("openId") String openId) {
        if (StringUtils.isEmpty(openId)) {
            return ResponseResult.fail(INVALID_PARAM);
        }
        Member member = memberService.findByOpenId(openId);

        if (member != null) {
            return ResponseResult.success(member.getBalance());
        }
        return ResponseResult.fail(PERMISSION_DENIED);
    }

    /**
     * 微信小程序钱包充值
     * @param dto
     * @param request
     * @return
     */
    @PostMapping("prepay")
    public WxPayMpOrderResult walletPrepay(@RequestBody WalletDTO dto, HttpServletRequest request) {
        String openId = dto.getOpenId();
        String amount = dto.getAmount();
        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(amount)) {
            return WxPayMpOrderResult.builder().build();
        }
        return walletService.walletPrepay(dto, request);
    }

    /**
     * 消费记录
     *
     * @param json
     * @return
     */
    @PostMapping("record")
    public Result recordList(@RequestBody JSONObject json) {
        Map<String, Object> map = paymentComponent.paymentRecord(json);
        if (map == null) {
            return ResponseResult.fail(INVALID_PARAM);
        }
        return ResponseResult.success(walletService.recordList(map));
    }
}
