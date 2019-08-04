package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.domain.Tree;
import com.nuoze.cctower.common.util.MD5Utils;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.pojo.entity.Menu;
import com.nuoze.cctower.service.MenuService;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.nuoze.cctower.common.util.ShiroUtils.getUser;
import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;


/**
 * @author JiaShun
 * @date 2019-02-20 00:01
 */
@Data
@Controller
public class LoginController {

    @Autowired
    private MenuService menuService;

    @GetMapping({"/", ""})
    String welcome() {
        return "login";
    }

    @GetMapping("/index")
    String index(Model model) {
        List<Tree<Menu>> menus = menuService.listMenuTree(getUserId());
        model.addAttribute("menus", menus);
        model.addAttribute("name", getUser().getName());
        model.addAttribute("picUrl", "/img/CCTowerLogo.png");
        model.addAttribute("username", getUser().getUsername());
        return "index_v1";
    }

    @PostMapping("/login")
    @ResponseBody
    R ajaxLogin(String username, String password) {
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return R.ok();
        } catch (AuthenticationException e) {
            return R.error("用户名或密码错误");
        }
    }

    @GetMapping("/logout")
    String logout() {
        ShiroUtils.logout();
        return "login";
    }

    @GetMapping("/main")
    String main() {
        return "main";
    }

}
