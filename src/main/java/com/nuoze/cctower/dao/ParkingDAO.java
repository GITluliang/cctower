package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Parking;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface ParkingDAO extends BaseDAO<Parking> {

    int batchRemove(Long[] ids);

    List<Parking> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    List<Long> findByUserId(Long userId);

    List<Parking> findParkingByUser(Long userId);

    Parking findByUserIdAndParkingName(@Param("userId") Long userId, @Param("parkingName") String parkingName);

    Parking findByParkingName( @Param("parkingName") String parkingName);
}