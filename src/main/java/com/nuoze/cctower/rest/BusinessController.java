package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.*;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.pojo.vo.UserVO;
import com.nuoze.cctower.service.BusinessService;
import com.nuoze.cctower.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * @author JiaShun
 * @date 2019-03-13 00:51
 */
@Controller
@RequestMapping("/sys/business")
public class BusinessController {

    private String prefix = "system/business/";

    @Autowired
    private BusinessService businessService;

    @Autowired
    private UserService userService;
    @Autowired
    private IdComponent idComponent;

    @GetMapping()
    @RequiresPermissions("sys:business:business")
    String business() {
        return prefix + "business";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:business:parking")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        params.put("typeArr" , Arrays.asList(1,2));
        Query query = new Query(params);
        return new PageUtils(businessService.list(query), businessService.count(query));
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:business:add")
    String add(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:business:edit")
    String edit(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "edit";
    }

    @RequiresPermissions("sys:user:add")
    @PostMapping("/save")
    @ResponseBody
    R save(User user) {
        user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        return businessService.save(user) > 0 ? R.ok() : R.error();

    }

    @RequiresPermissions("sys:user:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(UserVO vo) {
        return userService.update(vo) > 0 ? R.ok() : R.error();
    }

    @RequiresPermissions("sys:user:remove")
    @PostMapping("/remove")
    @ResponseBody
    R remove(Long id) {
        return userService.remove(id) > 0 ? R.ok() : R.error();
    }
}
