package com.nuoze.cctower.pojo.dto;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-05-13 00:07
 */
@Data
public class RenewCarPayDTO {

    private Long carId;
    private Long parkingId;
    private String cost;
    private String openId;
    private Integer monthCount;
}
