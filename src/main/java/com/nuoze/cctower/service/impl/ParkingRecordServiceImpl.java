package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.ParkingRecordDAO;
import com.nuoze.cctower.dao.PassagewayDAO;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.pojo.entity.Passageway;
import com.nuoze.cctower.pojo.vo.ParkingRecordVO;
import com.nuoze.cctower.service.ParkingRecordService;
import com.nuoze.cctower.service.ParkingService;
import com.nuoze.cctower.service.PassagewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;
import static com.nuoze.cctower.common.constant.Constant.PARKING_TRADING_RECORD_EXPEND_TYPE;

/**
 * @author JiaShun
 * @date 2019-04-06 12:58
 */
@Service
public class ParkingRecordServiceImpl implements ParkingRecordService {

    @Autowired
    private ParkingRecordDAO parkingRecordDAO;
    @Autowired
    private ParkingService parkingService ;
    @Autowired
    private PassagewayService passagewayService ;

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
                parkingRecordVO.setCost(parkingRecord.getCost().toString());
                parkingRecordVO.setInTime(DateUtils.formatDateTime(parkingRecord.getInTime()));
                parkingRecordVO.setOutTime(DateUtils.formatDateTime(new Date()));
                parkingRecordVO.setTakeMinutes(parkingRecord.getCostTime());
                parkingRecordVO.setCarNumber(parkingRecord.getCarNumber());
                Parking parking = parkingService.findById(parkingRecord.getParkingId());
                if (parking != null) {
                    parkingRecordVO.setParkingName(parking.getName());
                }
            } else {
                parkingRecordVO.setTakeMinutes(PARKING_TRADING_RECORD_EXPEND_TYPE);
            }
        }
        return parkingRecordVO ;
    }
}
