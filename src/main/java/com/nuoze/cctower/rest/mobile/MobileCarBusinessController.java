package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
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
@RequestMapping("/mobile/car/business")
public class MobileCarBusinessController {

    private String prefix = "h5/mobile/carBusiness/";

    @RequestMapping()
    public String carBusiness() {
        return prefix + "listShanghu";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carBusinessAuthZ() {
        return getSubject().isPermitted("sys:car:business") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
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
}
