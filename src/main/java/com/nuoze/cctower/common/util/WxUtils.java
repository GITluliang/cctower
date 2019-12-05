package com.nuoze.cctower.common.util;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.chanjar.weixin.common.error.WxErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-04-07 13:49
 */
@Slf4j
@Component
public class WxUtils {

    @Autowired
    private WxMaService wxService;

    /**
     * 小程序获取openid
     * @param code
     * @return
     */
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

    /**
     * 获取请求用户信息的access_token
     *
     * @param code
     * @return
     */
    public Map<String, String> getUserInfoAccessToken(String code) {
        String APPID = "wxc1487ff13e8c64df" ;
        String APPSECRET = "b535de3fc7cc12f923742d9bfcc26dee" ;
        JsonObject object = null;
        Map<String, String> data = new HashMap();
        try {
            String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                    APPID, APPSECRET, code);
            log.info("request accessToken from url: {}", url);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String tokens = EntityUtils.toString(httpEntity, "utf-8");
            Gson token_gson = new Gson();
            object = token_gson.fromJson(tokens, JsonObject.class);
            log.info("request accessToken success. [result={}]", object);
            data.put("openid", String.valueOf(object.get("openid")).replaceAll("\"", ""));
            data.put("access_token",String.valueOf(object.get("access_token")).replaceAll("\"", ""));
        } catch (Exception ex) {
            log.error("fail to request wechat access token. [error={}]", ex);
        }
        return data;
    }
}
