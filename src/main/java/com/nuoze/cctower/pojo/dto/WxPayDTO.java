package com.nuoze.cctower.pojo.dto;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-01-23 00:00
 */
@Data
public class WxPayDTO {

    private String code;
    /**
     * 停车记录主键id
     */
    private Long recordId;

    private String money;

}
