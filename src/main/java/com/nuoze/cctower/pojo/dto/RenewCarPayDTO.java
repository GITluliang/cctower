package com.nuoze.cctower.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-05-13 00:07
 */
@Data
@Accessors(chain = true)
public class RenewCarPayDTO {

    private Long carId;
    private Long parkingId;
    private String cost;
    private String openId;
    private Integer monthCount;
}
