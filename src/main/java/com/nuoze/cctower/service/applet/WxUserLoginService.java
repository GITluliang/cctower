package com.nuoze.cctower.service.applet;

import com.nuoze.cctower.pojo.entity.User;

/**
 * @Authror luliang
 * @Date 2019-09-19 16:10
 */
public interface WxUserLoginService {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return User
     */
    User findByUsername(String username);
}
