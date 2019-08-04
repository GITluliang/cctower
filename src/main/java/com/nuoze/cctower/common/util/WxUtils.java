package com.nuoze.cctower.common.util;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @author JiaShun
 * @date 2019-04-07 13:49
 */
@Slf4j
@Component
public class WxUtils {

    @Autowired
    private WxMaService wxService;

    public String findOpenIdByCode(String code) {
        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            openId = result.getOpenid();
        } catch (WxErrorException e) {
            log.error("WeiXin login has exception: {}", e.getMessage());
        }
        return openId;
    }
}
