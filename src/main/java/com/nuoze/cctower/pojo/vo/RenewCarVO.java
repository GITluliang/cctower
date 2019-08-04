package com.nuoze.cctower.pojo.vo;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-05-12 21:07
 */
@Data
public class RenewCarVO {

    private Long carId;
    private Long parkingId;
    private String parkingName;
    private String monthlyParkingStart;
    private String monthlyParkingEnd;
    private String monthlyPrice;
}
