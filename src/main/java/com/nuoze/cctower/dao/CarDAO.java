package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Car;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * long-long-car
 * @author JiaShun
 */
@Repository
public interface CarDAO extends BaseDAO<Car> {

    List<Car> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    /**
     * 根据车场id和车牌号，查询车辆
     * @param parkingId 车场id
     * @param carNumber 车牌号
     * @return Car
     */
    Car findByParkingIdAndCarNumber(@Param("parkingId") Long parkingId, @Param("carNumber") String carNumber);

    int batchRemove(Long[] ids);

    Long selectByNumberAndOpenId(@Param("number") String number, @Param("openId") String openId);

    Car findByCarNumberAndParkingType(@Param("carNumber") String carNumber, @Param("parkingType") Integer parkingType);

    List<Car> findByParkingIdAndParkingType(@Param("parkingId") Long parkingId, @Param("parkingType") Integer parkingType);
}