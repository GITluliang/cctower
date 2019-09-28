package com.nuoze.cctower.rest;

import com.nuoze.cctower.component.IdComponent;

import java.util.List;
import java.util.Map;

import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.pojo.dto.PassagewayDTO;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.Passageway;
import com.nuoze.cctower.service.PassagewayService;
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

import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;


/**
 * 通道
 *
 * @author JiaShun
 * @date 2019-03-17 12:07:24
 */

@Controller
@RequestMapping("/sys/passageway")
public class PassagewayController {

    private String prefix = "system/passageway/";

    @Autowired
    private PassagewayService passagewayService;
    @Autowired
    private IdComponent idComponent;

    @GetMapping()
    @RequiresPermissions("sys:passageway:passageway")
    String passageway() {
        return prefix + "passageway";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:passageway:passageway")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params = idComponent.buildParams(params);
        if (params.isEmpty()) {
            return new PageUtils(EMPTY_LIST, 0);
        }
        //查询列表数据
        Query query = new Query(params);
        List<PassagewayDTO> passagewayList = passagewayService.list(query);
        int total = passagewayService.count(query);
        return new PageUtils(passagewayList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:passageway:add")
    String add(Model model) {
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys:passageway:edit")
    String edit(@PathVariable Long id, Model model) {
        Passageway passageway = passagewayService.findById(id);
        model.addAttribute("passageway", passageway);
        List<Parking> parkingList = idComponent.getParkingList();
        model.addAttribute("parkingList", parkingList);
        return prefix + "edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("sys:passageway:add")
    public R save(Passageway passageway) {
        return passagewayService.save(passageway) > 0 ? R.ok() : R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("sys:passageway:edit")
    public R update(Passageway passageway) {
        passagewayService.update(passageway);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:passageway:remove")
    public R remove(Long id) {
        return passagewayService.remove(id) > 0 ? R.ok() : R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("sys:passageway:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        passagewayService.batchRemove(ids);
        return R.ok();
    }

}
