package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.dao.ParkingRecordDAO;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.service.ParkingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JiaShun
 * @date 2019-04-06 12:58
 */
@Service
public class ParkingRecordServiceImpl implements ParkingRecordService {

    @Autowired
    private ParkingRecordDAO parkingRecordDAO;

    @Override
    public ParkingRecord findByCarNumberAndStatus(String carNumber) {
        return parkingRecordDAO.findByCarNumberAndStatus(carNumber);
    }

    @Override
    public ParkingRecord findById(Long recordId) {
        return parkingRecordDAO.selectByPrimaryKey(recordId);
    }

    @Override
    public int update(ParkingRecord parkingRecord) {
        return parkingRecordDAO.updateByPrimaryKeySelective(parkingRecord);
    }

    @Override
    public ParkingRecord findByOrderSn(String orderSn) {
        return parkingRecordDAO.findByOrderSn(orderSn);
    }
}
