package com.nuoze.cctower.service.impl.applet;

import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.service.applet.WxParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Authror luliang
 * @Date 2019-09-19 21:15
 */
@Service
public class WxParkingServiceImpl implements WxParkingService {
    @Autowired
    private ParkingDAO parkingDAO ;

    @Override
    public List<Parking> findParkingByUser(Long userId) {
        return parkingDAO.findParkingByUser(userId);
    }
}
