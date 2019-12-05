package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.nuoze.cctower.common.util.ShiroUtils.getSubject;

/**
 * @Author luliang
 * @Date 2019-12-02 11:06
 */
@Controller
@RequestMapping("/mobile/car/vip")
public class MobileCarVipController {
    private String prefix = "h5/mobile/carVip/";
    @Autowired
    private CarService carService;

    @RequestMapping()
    public String carVIP() {
        return prefix + "listVip";
    }

    @RequestMapping("detail/{id}")
    public String carVIPDetail(@PathVariable Long id, Model model) {
        model.addAttribute(id);
        return prefix + "detailVip";
    }
    @RequestMapping("add")
    public String carVIPAdd() {
        return prefix + "addVip";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carVIPAuthZ() {
        return getSubject().isPermitted("sys:car:vip") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
    }

    @RequestMapping("/edit/{id}")
    public String carVIPEdit(@PathVariable Long id, Model model) {
        model.addAttribute(carService.findById(id));
        return prefix + "editVip";
    }
}
