package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Car;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * long-long-car
 *
 * @author JiaShun
 */
@Repository
public interface CarDAO extends BaseDAO<Car> {

    List<Car> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    /**
     * 模糊查询
     *
     * @param map
     * @return
     */
    List<Car> listLike(Map<String, Object> map);

    /**
     * 模糊查询
     *
     * @param map
     * @return
     */
    int countLike(Map<String, Object> map);

    /**
     * 根据车场id和车牌号，查询车辆
     *
     * @param parkingId 车场id
     * @param carNumber 车牌号
     * @return Car
     */
    Car findByParkingIdAndCarNumber(@Param("parkingId") Long parkingId, @Param("carNumber") String carNumber);

    int batchRemove(Long[] ids);

    Long selectByNumberAndOpenId(@Param("number") String number, @Param("openId") String openId);

    /**
     * 通过车牌号和车辆属性查找车辆
     *
     * @param carNumber   车牌号
     * @param parkingType 车辆属性 0:临时车 1:包月 2:VIP 3:商户车辆
     * @return Car
     */
    Car findByCarNumberAndParkingType(@Param("carNumber") String carNumber, @Param("parkingType") Integer parkingType);

    List<Car> findByParkingIdAndParkingType(@Param("parkingId") Long parkingId, @Param("parkingType") Integer parkingType);

    /**
     * 根据UUID进行更新
     * @param car
     * @return
     */
    int updateByUuid(Car car);

    /**
     * 根据UUID进行删除
     * @param ids
     * @return
     */
    int batchRemoveByUuid(String[] uuids);

    /**
     * 根据UUID进行查询
     * @param uuid
     * @return
     */
    Car findByUuid(String uuid);
}