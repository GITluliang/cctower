package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.entity.ParkingRecord;

/**
 * @author JiaShun
 * @date 2019-04-06 12:58
 */
public interface ParkingRecordService {

    ParkingRecord findByCarNumberAndStatus(String carNumber);

    ParkingRecord findById(Long recordId);

    int update(ParkingRecord parkingRecord);

    ParkingRecord findByOrderSn(String orderSn);
}
