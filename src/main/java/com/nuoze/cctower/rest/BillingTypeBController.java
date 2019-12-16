package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.component.BillingComponent;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.pojo.dto.BillingDetailDTO;
import com.nuoze.cctower.pojo.entity.BillingDetail;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.service.BillingDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * @author JiaShun
 * @date 2019-07-03 23:09
 */
@Controller
@RequestMapping("/sys/billing/typeB")
public class BillingTypeBController {

    private String prefix = "system/billingTypeB/";

    @Autowired
    private BillingDetailService billingService;
    @Autowired
    private IdComponent idComponent;
    @Autowired
    private BillingComponent checkComponent;

    @GetMapping()
    @RequiresPermissions("sys:billingDetailB:billingDetail")
    String billingTypeB(Model model) {
        model.addAttribute("parkingList", idComponent.getParkingList());
        return prefix + "detail";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:billingDetailB:billingDetail")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        params.put("type", 2);
        //查询列表数据
        Query query = new Query(params);
        List<BillingDetailDTO> billingList = billingService.list(query);
        int total = billingService.count(query);
        return new PageUtils(billingList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:billingDetailB:add")
    String add(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:billingDetailB:edit")
    String edit(@PathVariable Long id, Model model) {
        BillingDetail billing = billingService.findById(id);
        model.addAttribute("detail", billing);
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("sys:billingDetailB:add")
    public R save(BillingDetail billing) {
        billing.setType(2);
        R r = checkComponent.billingParkingIdCheck(billing.getParkingId(), false);
        if (r == null) {
            r = checkComponent.billingParamsCheck(billing);
        }
        return r != null ? r : billingService.save(billing) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:billingDetailB:edit")
    public R update(BillingDetail billing) {
        billingService.update(billing);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:billingDetailB:remove")
    public R remove(Long id) {
        if (billingService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:billingDetailB:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        billingService.batchRemove(ids);
        return R.ok();
    }
}
