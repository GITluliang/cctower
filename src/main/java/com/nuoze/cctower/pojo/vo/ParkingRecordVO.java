package com.nuoze.cctower.pojo.vo;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-04-06 14:50
 */
@Data
public class ParkingRecordVO {
    //ParkingRecord 停车记录ID
    private Long recordId;
    //入场时间
    private String inTime;
    //出厂时间
    private String outTime;
    //车费
    private String cost;
    //停车时长
    private Integer takeMinutes;
}
