package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
import com.nuoze.cctower.service.CarService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.nuoze.cctower.common.util.ShiroUtils.getSubject;

/**
 * @Author luliang
 * @Date 2019-12-02 11:03
 */
@Controller
@RequestMapping("/mobile/car/business")
public class MobileCarBusinessController {

    private String prefix = "h5/mobile/carBusiness/";
    @Autowired
    private CarService carService;

    @RequestMapping()
    public String carBusiness() {
        return prefix + "listShanghu";
    }

    @RequestMapping("detail/{id}")
    public String carBusinessDetail(@PathVariable Long id, Model model) {
        model.addAttribute(id);
        return prefix + "detailShanghu";
    }

    @RequestMapping("add")
    public String carBusinessAdd() {
        return prefix + "addShanghu";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carBusinessAuthZ() {
        return getSubject().isPermitted("sys:car:business") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
    }

    @RequestMapping("/edit/{id}")
    public String carBusinessEdit(@PathVariable Long id, Model model) {
        model.addAttribute(carService.findById(id));
        return prefix + "editShanghu";
    }
}
