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

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
        Integer count = parkingRecordDAO.countByParkingId(map);
        List list = map(parkingRecordDAO.listByParkingId(map));
        return new MutablePair<>(count, list);
    }

    private List map(List<ParkingRecord> list) {
        List<FinanceVO> financeVOS = new CopyOnWriteArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (ParkingRecord ps : list) {
                FinanceVO fv = new FinanceVO();
                if (ps.getParkingId() != null) {
                    Parking parking = parkingDAO.selectByPrimaryKey(ps.getParkingId());
                    if (parking != null && parking.getName() != null) {
                        fv.setParkingName(parking.getName());
                    }
                }
                if (ps.getEntranceId() != null) {
                    Passageway passageway = passagewayDAO.selectByPrimaryKey(ps.getEntranceId());
                    if (passageway != null && passageway.getName() != null) {
                        fv.setEntranceName(passageway.getName());
                    }
                }
                if (ps.getExitId() != null) {
                    Passageway passageway = passagewayDAO.selectByPrimaryKey(ps.getExitId());
                    if (passageway != null && passageway.getName() != null) {
                        fv.setExitName(passageway.getName());
                    }
                }
                if (ps.getInTime() != null) {
                    fv.setInTime(DateUtils.formatDateTime(ps.getInTime()));
                }
                if (ps.getOutTime() != null) {
                    fv.setOutTime(DateUtils.formatDateTime(ps.getOutTime()));
                }
                financeVOS.add(fv.setCost(ps.getCost() != null ? ps.getCost() : EMPTY_MONEY)
                        .setCostTime(ps.getCostTime() != null ? String.valueOf(ps.getCostTime()) : String.valueOf(0))
                        .setPayType(ps.getPayType())
                        .setCarNumber(ps.getCarNumber())
                        .setStatus(ps.getStatus())
                        .setPayStatus(ps.getPayStatus())
                );
            }
        }
        return financeVOS;
    }
}
