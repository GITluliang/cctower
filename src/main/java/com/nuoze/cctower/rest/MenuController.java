package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.domain.Tree;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.pojo.entity.Menu;
import com.nuoze.cctower.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 12:47
 */
@RequestMapping("/sys/menu")
@Controller
public class MenuController {

    String prefix = "system/menu/";

    @Autowired
    private MenuService menuService;

    @RequiresPermissions("sys:menu:menu")
    @GetMapping()
    String menu() {
        return prefix + "menu";
    }

    @RequiresPermissions("sys:menu:menu")
    @GetMapping("list")
    @ResponseBody
    List<Menu> list(@RequestParam Map<String, Object> params) {
        return menuService.list(params);
    }

    @RequiresPermissions("sys:menu:add")
    @GetMapping("/add/{pId}")
    String add(Model model, @PathVariable Long pId) {
        addParentName(model, pId);
        return prefix + "add";
    }

    @RequiresPermissions("sys:menu:edit")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable Long id) {
        Menu menu = menuService.findById(id);
        Long pId = menu.getParentId();
        addParentName(model, pId);
        model.addAttribute("menu", menu);
        return prefix + "edit";
    }

    @RequiresPermissions("sys:menu:add")
    @PostMapping("/save")
    @ResponseBody
    R save(Menu menu) {
        return menuService.save(menu) > 0 ? R.ok() : R.error(1, "保存失败");
    }

    private void addParentName(Model model, Long pId) {
        model.addAttribute("pId", pId);
        if (0 == pId) {
            model.addAttribute("pName", "根目录");
        } else {
            model.addAttribute("pName", menuService.findById(pId).getName());
        }
    }

    @RequiresPermissions("sys:menu:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(Menu menu) {
        return menuService.update(menu) > 0 ? R.ok() : R.error(1, "更新失败");
    }

    @RequiresPermissions("sys:menu:remove")
    @PostMapping("/remove")
    @ResponseBody
    R remove(Long id) {
        return menuService.remove(id) > 0 ? R.ok() : R.error(1, "删除失败");
    }

    @GetMapping("/tree")
    @ResponseBody
    Tree<Menu> tree() {
        return menuService.getTree(null);
    }

    @GetMapping("/tree/{roleId}")
    @ResponseBody
    Tree<Menu> tree(@PathVariable Long roleId) {
        return menuService.getTree(roleId);
    }

}
