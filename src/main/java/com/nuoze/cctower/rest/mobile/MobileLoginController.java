package com.nuoze.cctower.rest.mobile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 手机端用户名、密码登录
 *  进行月租、商户、VIP车辆录入
 * @Author luliang
 * @Date 2019-11-04 15:23
 */
@Controller
@RequestMapping("mobile")
public class MobileLoginController {
    private String prefix = "h5/mobile/";

    @RequestMapping("login")
    public String sweepCodeRed() {
        return prefix + "userBind" ;
    }

    @RequestMapping("register")
    public String register(String username, String password) {
        System.out.println("[************]" + username + "," + password);
        return prefix + "home" ;
    }
}
