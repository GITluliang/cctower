package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
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
 * @Date 2019-12-02 10:59
 */
@Controller
@RequestMapping("/mobile/car/long")
public class MobileCarLongController {
    private String prefix = "h5/mobile/carLong/";
    @Autowired
    private CarService carService;

    @RequestMapping()
    public String carLong() {
        return prefix + "listYuezu";
    }

    @RequestMapping("detail/{id}")
    public String carLongDetail(@PathVariable Long id, Model model) {
        model.addAttribute(id);
        return prefix + "detailYuezu";
    }
    @RequestMapping("add")
    public String carLongAdd() {
        return prefix + "addYuezu";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carLongAuthZ() {
        return getSubject().isPermitted("sys:car:car") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
    }

    @RequestMapping("/edit/{id}")
    public String carLongEdit(@PathVariable Long id, Model model) {
        model.addAttribute(carService.findById(id));
        return prefix + "editYuezu";
    }
}
