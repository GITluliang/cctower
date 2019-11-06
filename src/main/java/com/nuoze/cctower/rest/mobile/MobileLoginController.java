package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.service.CarService;
import com.nuoze.cctower.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("mobile")
public class MobileLoginController {
    private String prefix = "h5/mobile/";

    @Autowired
    private CarService carService ;
    @Autowired
    private ParkingService parkingService;

    @RequestMapping("login")
    public String sweepCodeRed() {
        if (getSubject().getPrincipal() == null) {
            return prefix + "userBind";
        }
        return "redirect:/mobile/home";
    }

    @RequestMapping("home")
    public String home() {
        return prefix + "home";
    }

    @ResponseBody
    @RequestMapping("car/long/authz")
    public Result carLongAuthZ() {
        return getSubject().isPermitted("sys:car:car") ? ResponseResult.success() : ResponseResult.fail(401,"没有权限");
    }

    @ResponseBody
    @RequestMapping("car/business/authz")
    public Result carBusinessAuthZ() {
        return getSubject().isPermitted("sys:car:business") ? ResponseResult.success() : ResponseResult.fail(401,"没有权限");
    }

    @ResponseBody
    @RequestMapping("car/vip/authz")
    public Result carVIPAuthZ() {
        return getSubject().isPermitted("sys:car:vip") ? ResponseResult.success() : ResponseResult.fail(401,"没有权限");
    }

    @RequestMapping("car/long")
    public String carLong() {
        return prefix + "listYuezu";
    }

    @RequestMapping("car/business")
    public String carBusiness() {
        return prefix + "listShanghu";
    }

    @RequestMapping("car/vip")
    public String carVIP() {
        return prefix + "listVip";
    }

    @RequestMapping("car/long/detail/{id}")
    public String carLongDetail(@PathVariable Long id, Model model) {
        model.addAttribute(id);
        return prefix + "detailYuezu";
    }

    @RequestMapping("car/business/detail/{id}")
    public String carBusinessDetail(@PathVariable Long id, Model model) {
        model.addAttribute(id);
        return prefix + "detailShanghu";
    }

    @RequestMapping("car/vip/detail/{id}")
    public String carVIPDetail(@PathVariable Long id, Model model) {
        model.addAttribute(id);
        return prefix + "detailVip";
    }

    @ResponseBody
    @RequestMapping("parking/{id}")
    public Result findById(@PathVariable Long id) {
        return ResponseResult.success(carService.findById(id));
    }

    @RequestMapping("car/long/add")
    public String carLongAdd() {
        return prefix + "addYuezu";
    }

    @RequestMapping("car/business/add")
    public String carBusinessAdd() {
        return prefix + "addShanghu";
    }

    @RequestMapping("car/vip/add")
    public String carVIPAdd() {
        return prefix + "addVip";
    }

    @ResponseBody
    @RequestMapping("getParking")
    public Result findByUserParking() {
        return ResponseResult.success(parkingService.findParkingByUser(getUserId()));
    }
}
