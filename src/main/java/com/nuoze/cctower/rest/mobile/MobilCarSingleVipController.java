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
 * @Date 2020-01-07 16:49
 */
@Controller
@RequestMapping("/mobile/car/singleVip")
public class MobilCarSingleVipController {
    private String prefix = "h5/mobile/singleVip/";
    @Autowired
    private CarService carService;

    @RequestMapping()
    public String carLong() {
        return prefix + "listSingleVip";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carLongAuthZ() {
        return getSubject().isPermitted("sys:car:singleVip") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
    }
    @RequestMapping("add")
    public String carVIPAdd() {
        return prefix + "addSingleVip";
    }

    @RequestMapping("detail/{id}")
    public String carVIPDetail(@PathVariable Long id, Model model) {
        model.addAttribute(id);
        return prefix + "detailSingleVip";
    }

    @RequestMapping("/edit/{id}")
    public String carVIPEdit(@PathVariable Long id, Model model) {
        model.addAttribute(carService.findById(id));
        return prefix + "editSingleVip";
    }
}
