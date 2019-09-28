package com.nuoze.cctower.rest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.dao.AccountDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.entity.Account;
import com.nuoze.cctower.pojo.entity.Parking;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nuoze.cctower.service.ParkingService;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;

/**
 * 停车场管理
 *
 * @author JiaShun
 * @date 2019-03-10 16:27:49
 */

@Controller
@RequestMapping("/sys/parking")
public class ParkingController {

    private String prefix = "system/parking/";

    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ParkingDAO parkingDAO;
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping()
    @RequiresPermissions("sys:parking:parking")
    String parking() {
        return prefix + "parking";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:parking:parking")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<Parking> parkingList = parkingService.list(query);
        int total = parkingService.count(query);
        return new PageUtils(parkingList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:parking:add")
    String add() {
        return prefix + "add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:parking:edit")
    String edit(@PathVariable Long id, Model model) {
        Parking parking = parkingService.findById(id);
        model.addAttribute("parking", parking);
        return prefix + "edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("sys:parking:add")
    public R save(Parking parking) {
        if (StringUtils.isNotBlank(parking.getName())) {
            if (checkParkName(parking.getName())) {
                return R.error(201, "此停车场名字已存在");
            }
        }
        return parkingService.save(parking) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:parking:edit")
    public R update(Parking parking) {
        if (StringUtils.isNotBlank(parking.getName())) {
            if (checkParkName(parking.getName())) {
                return R.error(201, "此停车场名字已存在");
            }
        }
        return parkingService.update(parking) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:parking:remove")
    public R remove(Long id) {
        Account account = accountDAO.selectByParkingId(id);
        if (account != null && !account.getBalance().equals(EMPTY_MONEY)) {
            return R.error(201, "此停车场已产生交易记录，不能删除");
        }
        return parkingService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:parking:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        return parkingService.batchRemove(ids) > 0 ? R.ok() : R.error();
    }

    private boolean checkParkName(String parkingName) {
        Long userId = ShiroUtils.getUser().getId();
        return parkingDAO.findByUserIdAndParkingName(userId, parkingName) != null;
    }

}
