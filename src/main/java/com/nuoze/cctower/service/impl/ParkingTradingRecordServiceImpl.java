package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.enums.IncomeType;
import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.ParkingTradingRecordDAO;
import com.nuoze.cctower.pojo.entity.ParkingTradingRecord;
import com.nuoze.cctower.pojo.vo.ParkingTradingRecordVO;
import com.nuoze.cctower.service.ParkingTradingRecordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-05-16 00:01
 */
@Service
public class ParkingTradingRecordServiceImpl implements ParkingTradingRecordService {

    @Autowired
    private ParkingTradingRecordDAO parkingTradingRecordDAO;
    @Autowired
    private ParkingDAO parkingDAO;

    @Override
    public Pair<Integer, List<ParkingTradingRecordVO>> list(Map<String, Object> map) {
        List<ParkingTradingRecordVO> recordVOS = new ArrayList<>();
        List<ParkingTradingRecord> list = parkingTradingRecordDAO.listByParkingId(map);
        int count = parkingTradingRecordDAO.countByParkingId(map);
        if (!CollectionUtils.isEmpty(list)) {
            for (ParkingTradingRecord record : list) {
                ParkingTradingRecordVO vo = new ParkingTradingRecordVO();
                BeanUtils.copyProperties(record, vo);
                Long parkingId = record.getParkingId();
                String parkingName = parkingDAO.selectByPrimaryKey(parkingId).getName();
                String createDate = DateUtils.formatDateTime(record.getPayTime());
                String incomeType = record.getIncomeType();
                if (StringUtils.isNotBlank(incomeType)) {
                    for (IncomeType income : IncomeType.values()) {
                        if (incomeType.equals(income.name())) {
                            String incomeTypeValue = income.getValue();
                            vo.setIncomeTypeValue(incomeTypeValue);
                        }
                    }
                }
                vo.setParkingName(parkingName);
                vo.setCreateDate(createDate);
                recordVOS.add(vo);
            }
        }
        return new MutablePair<>(count, recordVOS);
    }
}
