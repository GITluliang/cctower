package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.ParkingRecordDAO;
import com.nuoze.cctower.dao.PassagewayDAO;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.pojo.entity.Passageway;
import com.nuoze.cctower.pojo.vo.FinanceVO;
import com.nuoze.cctower.service.FinanceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;

/**
 * @author JiaShun
 * @date 2019-05-01 17:24
 */
@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private ParkingRecordDAO parkingRecordDAO;
    @Autowired
    private ParkingDAO parkingDAO;
    @Autowired
    private PassagewayDAO passagewayDAO;

    @Override
    public Pair<Integer, List<FinanceVO>> list(Map<String, Object> map) {
        List<FinanceVO> financeVOS = new ArrayList<>();
        List<ParkingRecord> list = parkingRecordDAO.listByParkingId(map);
        int count = parkingRecordDAO.countByParkingId(map);
        if (!CollectionUtils.isEmpty(list)) {
            for (ParkingRecord ps : list) {
                FinanceVO fv = new FinanceVO();
                Long parkingId = ps.getParkingId();
                if (parkingId != null) {
                    Parking parking = parkingDAO.selectByPrimaryKey(parkingId);
                    if (parking != null && parking.getName() != null) {
                        fv.setParkingName(parking.getName());
                    }
                }
                Long entranceId = ps.getEntranceId();
                if (entranceId != null) {
                    Passageway passageway = passagewayDAO.selectByPrimaryKey(entranceId);
                    if (passageway != null && passageway.getName() != null) {
                        fv.setEntranceName(passageway.getName());
                    }
                }
                Long exitId = ps.getExitId();
                if (exitId != null) {
                    Passageway passageway = passagewayDAO.selectByPrimaryKey(exitId);
                    if (passageway != null && passageway.getName() != null) {
                        fv.setExitName(passageway.getName());
                    }
                }
                Date inTime = ps.getInTime();
                if (inTime != null) {
                    fv.setInTime(DateUtils.formatDateTime(inTime));
                }
                Date outTime = ps.getOutTime();
                if (outTime != null) {
                    fv.setOutTime(DateUtils.formatDateTime(outTime));
                }
                fv.setCost(ps.getCost() != null ? ps.getCost() : EMPTY_MONEY);
                fv.setCostTime(ps.getCostTime() != null ? String.valueOf(ps.getCostTime()) : String.valueOf(0));
                fv.setPayType(ps.getPayType());
                fv.setCarNumber(ps.getCarNumber());
                financeVOS.add(fv);
            }
        }
        return new MutablePair<>(count, financeVOS);
    }
}
