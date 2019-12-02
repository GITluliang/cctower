package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.service.CarService;
import com.nuoze.cctower.service.ParkingService;
import com.nuoze.cctower.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.nuoze.cctower.common.util.ShiroUtils.getSubject;
import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;

/**
 * 手机端用户名、密码登录
 * 进行月租、商户、VIP车辆录入
 *
 * @Author luliang
 * @Date 2019-11-04 15:23
 */
@Controller
@RequestMapping("/mobile")
public class MobileLoginController {
    private String prefix = "h5/mobile/";

    @Autowired
    private CarService carService;
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private UserService userService;

    @RequestMapping("login")
    public String sweepCodeRed() {
        if (getSubject().getPrincipal() == null) {
            return prefix + "userBind";
        }
        return "forward:/mobile/home";
    }

    @RequestMapping("logout")
    String logout() {
        ShiroUtils.logout();
        return "redirect:/mobile/login";
    }

    @RequestMapping("home")
    public String home() {
        return prefix + "home";
    }

    @RequestMapping("setting")
    public String setting() {
        return prefix + "setting";
    }

    @ResponseBody
    @RequestMapping("parking/{id}")
    public Result findById(@PathVariable Long id) {
        return ResponseResult.success(carService.findById(id));
    }

    @ResponseBody
    @RequestMapping("getParking")
    public Result findByUserParking() {
        return ResponseResult.success(parkingService.findParkingByUser(getUserId()));
    }

    @ResponseBody
    @RequestMapping("getSetting")
    public Result findByUserSetting() {
        return ResponseResult.success(userService.findById(getUserId()));
    }


}
