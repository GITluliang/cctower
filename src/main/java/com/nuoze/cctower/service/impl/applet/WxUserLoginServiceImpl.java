package com.nuoze.cctower.service.impl.applet;

import com.nuoze.cctower.dao.UserDAO;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.service.applet.WxUserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Authror luliang
 * @Date 2019-09-19 16:11
 */
@Service
public class WxUserLoginServiceImpl implements WxUserLoginService {

    @Autowired
    private UserDAO userDAO ;

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}
