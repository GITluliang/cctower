package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author luliang
 * @Date 2019-12-18 14:38
 */
@Data
@Accessors(chain = true)
public class PassagewayVO {
    /**
     * 停车场id
     */
    private Long parkingId;
    /**
     * 0：入口，1：出口
     */
    private Integer type;
    /**
     * IP地址
     */
    private String ip;
}
