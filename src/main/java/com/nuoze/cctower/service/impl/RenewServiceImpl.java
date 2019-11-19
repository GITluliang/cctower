package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.RenewRecordDAO;
import com.nuoze.cctower.pojo.entity.RenewRecord;
import com.nuoze.cctower.pojo.vo.RenewRecordVO;
import com.nuoze.cctower.service.RenewService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JiaShun
 * @date 2019-05-15 00:37
 */
@Service
public class RenewServiceImpl implements RenewService {

    @Autowired
    private RenewRecordDAO renewRecordDAO;
    @Autowired
    private ParkingDAO parkingDAO;

    @Override
    public Pair<Integer, List<RenewRecordVO>> list(Map<String, Object> map) {
        List<RenewRecordVO> recordVOS = new CopyOnWriteArrayList<>();
        List<RenewRecord> list = renewRecordDAO.listByParkingId(map);
        int count = renewRecordDAO.countByParkingId(map);
        if (!CollectionUtils.isEmpty(list)) {
            for (RenewRecord record : list) {
                RenewRecordVO vo = new RenewRecordVO();
                BeanUtils.copyProperties(record, vo);
                Long parkingId = record.getParkingId();
                String parkingName = parkingDAO.selectByPrimaryKey(parkingId).getName();
                String createDate = DateUtils.formatDateTime(record.getCreateTime());
                vo.setParkingName(parkingName);
                vo.setCreateDate(createDate);
                recordVOS.add(vo);
            }
        }
        return new MutablePair<>(count, recordVOS);
    }
}
