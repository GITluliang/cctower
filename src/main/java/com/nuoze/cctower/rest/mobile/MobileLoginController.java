package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.nuoze.cctower.common.util.ShiroUtils.getSubject;

/**
 * 手机端用户名、密码登录
 * 进行月租、商户、VIP车辆录入
 *
 * @Author luliang
 * @Date 2019-11-04 15:23
 */
@Controller
@RequestMapping("mobile")
public class MobileLoginController {
    private String prefix = "h5/mobile/";

    @RequestMapping("login")
    public String sweepCodeRed() {
        if (getSubject().getPrincipal() == null) {
            return prefix + "userBind";
        }
        return "forward:/mobile/home";
    }

    @RequestMapping("home")
    public String home() {
        return prefix + "home";
    }

    @ResponseBody
    @RequestMapping("car/long")
    public Result carLong() {
        return getSubject().isPermitted("sys:car:car") ? ResponseResult.success() : ResponseResult.fail(401,"没有权限");
    }

    @ResponseBody
    @RequestMapping("car/business")
    public Result carBusiness() {
        return getSubject().isPermitted("sys:car:business") ? ResponseResult.success() : ResponseResult.fail(401,"没有权限");
    }

    @ResponseBody
    @RequestMapping("car/vip")
    public Result carVIP() {
        return getSubject().isPermitted("sys:car:vip") ? ResponseResult.success() : ResponseResult.fail(401,"没有权限");
    }
}
