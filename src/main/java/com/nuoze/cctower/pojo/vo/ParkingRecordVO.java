package com.nuoze.cctower.pojo.vo;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-04-06 14:50
 */
@Data
public class ParkingRecordVO {

    private Long recordId;
    private String inTime;
    private String outTime;
    private String cost;
    private Integer takeMinutes;
}
