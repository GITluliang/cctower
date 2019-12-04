package com.nuoze.cctower.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author luliang
 * @Date 2019-12-04 12:17
 */
@Data
@Accessors(chain = true)
public class RechargeDTO {
    private String openId;
    private Long parkingId;
    private Long userId;
    private String cost;
}
