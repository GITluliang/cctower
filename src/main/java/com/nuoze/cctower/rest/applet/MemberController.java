package com.nuoze.cctower.rest.applet;

import com.nuoze.cctower.common.util.WxUtils;
import com.nuoze.cctower.pojo.dto.MemberDTO;
import com.nuoze.cctower.pojo.entity.Member;
import com.nuoze.cctower.service.applet.MemberService;
import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;
import static com.nuoze.cctower.common.result.ResultEnum.INVALID_PARAM;
import static com.nuoze.cctower.common.result.ResultEnum.WX_SERVER_ERROR;

/**
 * @author JiaShun
 * @date 2019-01-13 23:48
 */
@Slf4j
@RestController
@RequestMapping("applet/member")
public class MemberController {

    @Autowired
    private WxUtils wxUtils;

    @Autowired
    private MemberService memberService;

    @PostMapping("login")
    public Result login (@RequestBody MemberDTO dto) {
        String openId = dto.getOpenId();
        String code = dto.getCode();
        if (StringUtils.isEmpty(code)) {
            return ResponseResult.fail(INVALID_PARAM.getCode(), INVALID_PARAM.getMessage());
        }
        if (StringUtils.isEmpty(openId)) {
            openId = wxUtils.findOpenIdByCode(code);
        }
        Member member = this.memberService.findByOpenId(openId);
        if (member == null) {
            member = new Member();
            BeanUtils.copyProperties(dto, member);
            member.setBalance(EMPTY_MONEY);
            memberService.add(member);
        } else {
            memberService.update(member);
        }
        return ResponseResult.success(openId);
    }

    @GetMapping("openId")
    public Result getOpenIdByCode(@RequestParam("code") String code) {
        String openId = wxUtils.findOpenIdByCode(code);
        if (StringUtils.isEmpty(openId)) {
            log.error("[Member Controller] find openId by code error, code:{}", code);
            return ResponseResult.fail(WX_SERVER_ERROR);
        }
        return ResponseResult.success(openId);
    }

}
