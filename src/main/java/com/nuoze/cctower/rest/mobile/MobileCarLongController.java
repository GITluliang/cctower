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
 * @Date 2019-12-02 10:59
 */
@Controller
@RequestMapping("/mobile/car/long")
public class MobileCarLongController {
    private String prefix = "h5/mobile/carLong/";

    @ResponseBody
    @RequestMapping("authz")
    public Result carLongAuthZ() {
        return getSubject().isPermitted("sys:car:car") ? ResponseResult.success() : ResponseResult.fail(401, "没有权限");
    }

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
}
