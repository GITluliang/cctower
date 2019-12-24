package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.pojo.vo.FinanceVO;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface ParkingRecordDAO extends BaseDAO<ParkingRecord> {
    /**
     * 通过停车场id和车牌号查询停车记录
     * @param parkingId 停车场id
     * @param carNumber 车牌号
     * @return
     */
    ParkingRecord findByParkingIdAndCarNumberAndStatus(@Param("parkingId") Long parkingId, @Param("carNumber") String carNumber);

    ParkingRecord findByCarNumberAndStatus(@Param("carNumber") String carNumber);

    ParkingRecord findByOrderSn(@Param("orderSn") String orderSn);

    List<ParkingRecord> findByParkingId(Long parkingId);

    Integer countByParkingId(Map<String, Object> map);

    List<ParkingRecord> listByParkingId(Map<String, Object> map);

    ParkingRecord findByUuid(@Param("uuid") String uuid);

    /**
     * 通过停车场id和出口ip,查询待出场车辆
     * @param parkingId
     * @param exitId
     * @return
     */
    ParkingRecord findByParkingIdAndIp(@Param("parkingId") Long parkingId, @Param("exitId") Long exitId);

    /**
     * 查找支付宝订单号
     * @param orderSn
     * @return
     */
    ParkingRecord findByOrderSnAliPay(@Param("orderSn") String orderSn);
}