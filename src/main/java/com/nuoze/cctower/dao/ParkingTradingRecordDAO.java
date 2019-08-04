package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.ParkingTradingRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface ParkingTradingRecordDAO extends BaseDAO<ParkingTradingRecord> {

    List<ParkingTradingRecord> listByParkingId(Map<String, Object> map);

    int countByParkingId(Map<String, Object> map);
}