package com.nuoze.cctower.common.util;

import com.nuoze.cctower.pojo.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


/**
 * @author JiaShun
 * @date 2019-02-26 01:45
 */
public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static User getUser() {
        Object object = getSubject().getPrincipal();
        return (User)object;
    }

    public static Long getUserId() {
        return getUser().getId();
    }
    public static void logout() {
        getSubject().logout();
    }

}
