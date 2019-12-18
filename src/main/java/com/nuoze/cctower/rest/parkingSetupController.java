package com.nuoze.cctower.rest;

import com.nuoze.cctower.service.ParkingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author luliang
 * @Date 2019-12-18 12:28
 */
@Controller
@RequestMapping("/sys/parkingSetup")
public class parkingSetupController {
    private String prefix = "system/parkingSetup/";
    @Autowired
    private ParkingService parkingService;

    @GetMapping()
    @RequiresPermissions("sys:parkingSetup:parkingSetup")
    String parkingSetup() {
        return prefix + "parking";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:parking:edit")
    String edit(@PathVariable Long id, Model model) {
        model.addAttribute("parking", parkingService.findById(id));
        return prefix + "edit";
    }
}
