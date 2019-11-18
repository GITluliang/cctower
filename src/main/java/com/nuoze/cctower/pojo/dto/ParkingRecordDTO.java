package com.nuoze.cctower.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-01-22 23:01
 */
@Data
@Accessors(chain = true)
public class ParkingRecordDTO {
    private String inTime;
    private String outTime;
    private String unitPrice;
    private String cost;
    private Integer takeMinutes;
}
