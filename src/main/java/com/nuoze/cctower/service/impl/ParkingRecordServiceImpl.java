package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.dao.ParkingRecordDAO;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.pojo.vo.ParkingRecordVO;
import com.nuoze.cctower.service.ParkingRecordService;
import com.nuoze.cctower.service.ParkingService;
import com.nuoze.cctower.service.PassagewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author JiaShun
 * @date 2019-04-06 12:58
 */
@Service
public class ParkingRecordServiceImpl implements ParkingRecordService {

    @Autowired
    private ParkingRecordDAO parkingRecordDAO;
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private PassagewayService passagewayService;

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

    @Override
    public ParkingRecordVO findByParkingIdAndIp(Long parkingId, Long exitId) {
        ParkingRecordVO parkingRecordVO = new ParkingRecordVO();
        if (parkingService.findById(parkingId) != null && passagewayService.findById(exitId) != null) {
            ParkingRecord parkingRecord = parkingRecordDAO.findByParkingIdAndIp(parkingId, exitId);
            if (parkingRecord != null) {
                parkingRecordVO.setRecordId(parkingRecord.getId());
                parkingRecordVO.setCost(parkingRecord.getCost() == null ? "0.0" : String.valueOf(parkingRecord.getCost()));
                parkingRecordVO.setInTime(DateUtils.formatDateTime(parkingRecord.getInTime()));
                parkingRecordVO.setOutTime(DateUtils.formatDateTime(new Date()));
                Integer costTime = parkingRecord.getCostTime();
                parkingRecordVO.setTakeMinutes(costTime);
                parkingRecordVO.setTakeMinutesStr(costTime / 60 >= 1 ? costTime / 60 + "小时" + costTime % 60 + "分钟" : costTime + "分钟");
                parkingRecordVO.setCarNumber(parkingRecord.getCarNumber());
                Parking parking = parkingService.findById(parkingRecord.getParkingId());
                if (parking != null) {
                    parkingRecordVO.setParkingName(parking.getName());
                }
            }
        }
        return parkingRecordVO;
    }
}
