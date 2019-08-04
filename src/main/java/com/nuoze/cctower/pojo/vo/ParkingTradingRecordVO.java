package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.ParkingTradingRecord;
import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-05-16 00:09
 */
@Data
public class ParkingTradingRecordVO extends ParkingTradingRecord {

    private String incomeTypeValue;
    private String parkingName;
    private String createDate;
}
