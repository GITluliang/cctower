package com.nuoze.cctower.rest;

import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.entity.Account;
import com.nuoze.cctower.pojo.vo.AccountVO;
import com.nuoze.cctower.service.AccountService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * @author JiaShun
 * @date 2019-05-16 00:34
 */
@Controller
@RequestMapping("/sys/charge")
public class ChargeController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ParkingDAO parkingDAO;

    @GetMapping()
    @RequiresPermissions("sys:charge:charge")
    String parkingTradingRecord() {
        return "system/charge/charge";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:account:edit")
    String edit(@PathVariable Long id, Model model){
        Account account = accountService.get(id);
        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(account, vo);
        vo.setParkingName(parkingDAO.selectByPrimaryKey(account.getParkingId()).getName());
        model.addAttribute("account", vo);
        return "system/charge/edit";
    }


}
