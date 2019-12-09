package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.nuoze.cctower.common.util.ShiroUtils.getSubject;

/**
 * @Author luliang
 * @Date 2019-12-02 11:03
 */
@Controller
@RequestMapping("/mobile/car/timeCoupon")
public class MobileCarTimeCouponController {

    private String prefix = "h5/mobile/carTimeCoupon/";
    @Autowired
    private CarService carService;

    @RequestMapping()
    public String carTimeCoupon() {
        return prefix + "listTimeCoupon";
    }

    @RequestMapping("detail/{id}")
    public String carTimeCouponDetail(@PathVariable Long id, Model model) {
        model.addAttribute(id);
        return prefix + "detailTimeCoupon";
    }

    @RequestMapping("add")
    public String carTimeCouponAdd() {
        return prefix + "addTimeCoupon";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carTimeCouponAuthZ() {
        return getSubject().isPermitted("sys:car:timeCoupon") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
    }

    @RequestMapping("/edit/{id}")
    public String carTimeCouponEdit(@PathVariable Long id, Model model) {
        model.addAttribute(carService.findById(id));
        return prefix + "editTimeCoupon";
    }
}
