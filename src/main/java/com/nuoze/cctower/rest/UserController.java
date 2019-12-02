package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.MD5Utils;
import com.nuoze.cctower.common.util.PageUtils;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.pojo.entity.Role;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.pojo.vo.UserVO;
import com.nuoze.cctower.service.RoleService;
import com.nuoze.cctower.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;

/**
 * @author JiaShun
 * @date 2019-03-03 15:29
 */
@RequestMapping("/sys/user")
@Controller
public class UserController {

    private String prefix = "system/user/";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("sys:user:user")
    @GetMapping()
    String user() {
        return prefix + "user";
    }

    @GetMapping("list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<UserVO> userList = userService.list(query);
        int total = userService.count(query);
        return new PageUtils(userList, total);
    }

    @RequiresPermissions("sys:user:add")
    @RequestMapping("/add")
    String add(Model model) {
        List<Role> roles = roleService.list();
        model.addAttribute("roles", roles);
        return prefix + "add";
    }

    @RequiresPermissions("sys:user:edit")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable Long id) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        List<Role> roles = roleService.list(id);
        model.addAttribute("roles", roles);
        return prefix + "edit";
    }

    @RequiresPermissions("sys:user:add")
    @PostMapping("/save")
    @ResponseBody
    R save(UserVO vo) {
        vo.setPassword(MD5Utils.encrypt(vo.getUsername(), vo.getPassword()));
        return userService.save(vo) > 0 ? R.ok() : R.error();

    }

    @RequiresPermissions("sys:user:edit")
    @PostMapping("/update")
    @ResponseBody
    R update(UserVO vo) {
        return userService.update(vo) > 0 ? R.ok() : R.error();
    }


    @GetMapping("/personal")
    String personal(Model model) {
        User user = userService.findById(getUserId());
        model.addAttribute("user", user);
        return prefix + "personal";
    }

    @RequiresPermissions("sys:user:edit")
    @PostMapping("/updatePersonal")
    @ResponseBody
    R updatePersonal(User user) {
        return userService.updatePersonal(user) > 0 ? R.ok() : R.error();
    }


    @RequiresPermissions("sys:user:remove")
    @PostMapping("/remove")
    @ResponseBody
    R remove(Long id) {
        return userService.remove(id) > 0 ? R.ok() : R.error();
    }

    @RequiresPermissions("sys:user:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") Long[] userIds) {
        return userService.batchRemove(userIds) > 0 ? R.ok() : R.error();
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false
        return !userService.exit(params);
    }

    @RequiresPermissions("sys:user:resetPwd")
    @GetMapping("/resetPwd/{id}")
    String resetPwd(@PathVariable Long id, Model model) {
        User user = new User();
        user.setId(id);
        model.addAttribute("user", user);
        return prefix + "reset_pwd";
    }

    @PostMapping("/resetPwd")
    @ResponseBody
    R resetPwd(UserVO vo) {
        try {
            userService.resetPwd(vo);
            return R.ok();
        } catch (Exception e) {
            return R.error(1, e.getMessage());
        }

    }

    @RequiresPermissions("sys:user:resetPwd")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    R adminResetPwd(UserVO vo) {
        try {
            userService.adminResetPwd(vo);
            return R.ok();
        } catch (Exception e) {
            return R.error(1, e.getMessage());
        }

    }

}
