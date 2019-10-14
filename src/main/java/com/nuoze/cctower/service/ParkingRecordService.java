package com.nuoze.cctower.service;

import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 停车记录表
 *
 * @author JiaShun
 * @date 2019-04-06 12:58
 */
public interface ParkingRecordService {
    /**
     * 根据车牌查询
     *
     * @param carNumber
     * @return
     */
    ParkingRecord findByCarNumberAndStatus(String carNumber);

    /**
     * 根据id查询
     *
     * @param recordId
     * @return
     */
    ParkingRecord findById(Long recordId);

    /**
     * 更新停车记录
     *
     * @param parkingRecord
     * @return
     */
    int update(ParkingRecord parkingRecord);

    /**
     * 根据订单号查询
     *
     * @param orderSn
     * @return
     */
    ParkingRecord findByOrderSn(String orderSn);

    /**
     * 通过停车场id和出口Id查询待出场车辆
     * @param parkingId
     * @param exitId
     * @return
     */
    String findByParkingIdAndIp(Long parkingId, Long exitId);
}
