package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.pojo.entity.Role;
import com.nuoze.cctower.pojo.vo.RoleVO;
import com.nuoze.cctower.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 20:54
 */
@RequestMapping("/sys/role")
@Controller
public class RoleController {

    String prefix = "system/role/";

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("sys:role:role")
    @GetMapping()
    String role() {
        return prefix + "role";
    }

    @RequiresPermissions("sys:role:role")
    @GetMapping("list")
    @ResponseBody
    List<Role> list() {
        return roleService.list();
    }

    @RequiresPermissions("sys:role:add")
    @GetMapping("add")
    String add() {
        return prefix + "add";
    }

    @RequiresPermissions("sys:role:edit")
    @GetMapping("edit/{id}")
    String edit(@PathVariable Long id, Model model) {
        Role role = roleService.findById(id);
        model.addAttribute("role", role);
        return prefix + "edit";
    }

    @RequiresPermissions("sys:role:save")
    @PostMapping("save")
    @ResponseBody
    R save(RoleVO vo) {
        return roleService.save(vo) > 0 ? R.ok() : R.error(1, "保存失败");
    }

    @RequiresPermissions("sys:role:edit")
    @PostMapping("update")
    @ResponseBody
    R update(RoleVO vo) {
        return roleService.update(vo) > 0 ? R.ok() : R.error(1, "更新失败");
    }

    @RequiresPermissions("sys:role:remove")
    @PostMapping("remove")
    @ResponseBody
    R remove(Long id) {
        return roleService.remove(id) > 0 ? R.ok() : R.error(1, "删除失败");
    }

    @RequiresPermissions("sys:role:batchRemove")
    @PostMapping("batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") Long[] ids) {
        return roleService.batchRemove(ids) > 0 ? R.ok() : R.error();
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false
        return !roleService.exit(params);
    }

}
