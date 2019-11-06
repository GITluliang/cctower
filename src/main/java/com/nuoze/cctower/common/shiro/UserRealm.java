package com.nuoze.cctower.common.shiro;

import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.dao.UserDAO;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.nuoze.cctower.common.constant.Constant.BUSINESS_FORBIDDEN_CAR;

/**
 * @author JiaShun
 * @date 2019-02-26 01:44
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserDAO userDAO;
	@Autowired
    private MenuService menuService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        log.info("[MobileRealm] Authorization 授权");
        Set<String> perms = menuService.listPerms(ShiroUtils.getUserId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(perms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("[MobileRealm] Authentication 认证");
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        User user = userDAO.findByUsername(username);
        if(user == null) {
            throw new UnknownAccountException("账号不存在！") ;
        }
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确！");
        }
        if (user.getStatus().compareTo(BUSINESS_FORBIDDEN_CAR) == 0) {
            throw new LockedAccountException(username + "账户已经被锁定！");
        }
        return new SimpleAuthenticationInfo(user, password, this.getName());
    }
}
