package com.nuoze.cctower.pojo.dto;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-01-23 00:00
 */
@Data
public class WxPayDTO {
    /**
     * 微信code，一次失效
     */
    private String code;
    /**
     * 停车记录主键id
     */
    private Long recordId;
    /**
     * 要支付的钱
     */
    private String money;
    /**
     * 微信用户唯一标识
     */
    private String openId ;

}
