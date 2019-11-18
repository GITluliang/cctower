package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-05-13 02:06
 */
@Data
@Accessors(chain = true)
public class RenewCarRecordVO {


    private String parkingCar;
    private String time;
    private String cost;
}
