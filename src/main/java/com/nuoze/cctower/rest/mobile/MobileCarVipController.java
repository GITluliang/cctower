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
 * @Date 2019-12-02 11:06
 */
@Controller
@RequestMapping("/mobile/car/vip")
public class MobileCarVipController {
    private String prefix = "h5/mobile/carVip/";

    @RequestMapping()
    public String carVIP() {
        return prefix + "listVip";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carVIPAuthZ() {
        return getSubject().isPermitted("sys:car:vip") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
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
}
