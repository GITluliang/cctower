package com.nuoze.cctower.service.impl.applet;

import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.dao.TopUpRecordDAO;
import com.nuoze.cctower.pojo.entity.TopUpRecord;
import com.nuoze.cctower.service.applet.TopUpRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-04-10 01:46
 */
@Service
public class TopUpRecordServiceImpl implements TopUpRecordService {

    @Autowired
    private TopUpRecordDAO topUpRecordDAO;

    @Override
    public int save(TopUpRecord topUpRecord) {
        topUpRecord.setCreateTime(new Date());
        topUpRecord.setUpdateTime(new Date());
        return topUpRecordDAO.insert(topUpRecord);
    }

    @Override
    public TopUpRecord findByOrderSn(String orderSn) {
        return topUpRecordDAO.findByOrderSn(orderSn);
    }

    @Override
    public int update(TopUpRecord topUpRecord) {
        topUpRecord.setUpdateTime(new Date());
        return topUpRecordDAO.updateByPrimaryKeySelective(topUpRecord);
    }

    @Override
    public List<TopUpRecord> findByOpenId(Map<String, Object> map) {
        return topUpRecordDAO.findByOpenId(map);
    }
}
