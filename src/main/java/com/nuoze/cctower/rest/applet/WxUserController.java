package com.nuoze.cctower.rest.applet;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.MD5Utils;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.service.applet.WxUserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信小程序用户名、密码登录
 *
 * @author luliang
 * @date 2019-09-19 13:19
 */
@RestController
@RequestMapping("applet/user")
public class WxUserController {

    @Autowired
    private WxUserLoginService userLoginService;

    @PostMapping("login")
    public Result login(@RequestBody User user) {
        User oldUser = userLoginService.findByUsername(user.getUsername());
        if (oldUser != null) {
            if (MD5Utils.encrypt(user.getUsername(), user.getPassword()).equals(oldUser.getPassword())) {
                return ResponseResult.success(oldUser.getId());
            }
        }
        return ResponseResult.fail(201, "用户名密码错误！");
    }
}
