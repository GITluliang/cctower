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

    ParkingRecord findByParkingIdAndCarNumberAndStatus(@Param("parkingId") Long parkingId, @Param("carNumber") String carNumber);

    ParkingRecord findByCarNumberAndStatus(@Param("carNumber") String carNumber);

    ParkingRecord findByOrderSn(@Param("orderSn") String orderSn);

    List<ParkingRecord> findByParkingId(Long parkingId);

    Integer countByParkingId(Map<String, Object> map);

    List<ParkingRecord> listByParkingId(Map<String, Object> map);

    ParkingRecord findByUuid(@Param("uuid") String uuid);
}