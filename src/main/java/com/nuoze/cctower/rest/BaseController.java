package com.nuoze.cctower.rest;

import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.pojo.entity.User;
import org.springframework.stereotype.Controller;

/**
 * @author JiaShun
 * @date 2019-02-26 00:25
 */
@Controller
public class BaseController {

    public User getUser() {
        return ShiroUtils.getUser();
    }

    public Long getUserId() {
        return getUser().getId();
    }

    public String getUsername() {
        return getUser().getUsername();
    }


}
