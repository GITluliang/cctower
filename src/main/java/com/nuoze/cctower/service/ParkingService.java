package com.nuoze.cctower.service;

import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.pojo.entity.Parking;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-11 22:58
 */
public interface ParkingService {

    List<Parking> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    Parking findById(Long id);

    int save(Parking parking);

    int update(Parking parking);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Parking> findParkingByUser(Long userId);
}
