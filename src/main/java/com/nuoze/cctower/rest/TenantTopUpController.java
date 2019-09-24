package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.pojo.vo.TenantTopUpVO;
import com.nuoze.cctower.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * @author JiaShun
 * @date 2019-05-04 12:11
 */
@Controller
@RequestMapping("/sys/tenantTopUp")
public class TenantTopUpController {

    @Autowired
    private IdComponent idComponent;
    @Autowired
    private UserService userService;
    @Autowired
    private ParkingDAO parkingDAO;

    @GetMapping()
    @RequiresPermissions("sys:tenantTopUp:tenantTopUp")
    String tenantTopUp(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return "system/tenantTopUp/tenantTopUp";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:depositRecord:depositRecord")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        params.put("type", 1);
        //查询列表数据
        Query query = new Query(params);
        Pair<Integer, List<TenantTopUpVO>> pair = userService.tenantTopUpList(query);
        return new PageUtils(pair.getRight(), pair.getLeft());
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:car:edit")
    String edit(@PathVariable Long id, Model model) {
        TenantTopUpVO vo = userToTopUpVO(id);
        model.addAttribute("tenantTopUp", vo);
        return "system/tenantTopUp/edit";
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:car:edit")
    public R update(TenantTopUpVO vo) {
        return userService.updateBalance(vo) > 0 ? R.ok() : R.error();
    }


    private TenantTopUpVO userToTopUpVO(Long id) {
        User user = userService.findById(id);
        TenantTopUpVO vo = new TenantTopUpVO();
        String parkingName = parkingDAO.selectByPrimaryKey(user.getParkingId()).getName();
        String businessName = user.getName();
        BigDecimal balance = user.getBalance();
        vo.setUserId(id);
        vo.setParkingName(parkingName);
        vo.setBusinessName(businessName);
        vo.setBalance(balance);
        return vo;
    }
}
