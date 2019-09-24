package com.nuoze.cctower.service.applet;

import com.nuoze.cctower.pojo.entity.Parking;

import java.util.List;

/**
 * @Authror luliang
 * @Date 2019-09-19 21:14
 */
public interface WxParkingService {

    List<Parking> findParkingByUser(Long userId);
}
