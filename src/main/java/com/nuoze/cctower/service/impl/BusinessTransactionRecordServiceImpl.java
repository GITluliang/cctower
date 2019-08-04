package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.dao.BusinessTransactionRecordDAO;
import com.nuoze.cctower.pojo.entity.BusinessTransactionRecord;
import com.nuoze.cctower.service.BusinessTransactionRecordService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-05-05 01:29
 */
@Service
public class BusinessTransactionRecordServiceImpl implements BusinessTransactionRecordService {

    @Autowired
    private BusinessTransactionRecordDAO businessTransactionRecordDAO;

    @Override
    public Pair<Integer, List<BusinessTransactionRecord>> list(Map<String, Object> map) {
        List<BusinessTransactionRecord> list = businessTransactionRecordDAO.list(map);
        int count = businessTransactionRecordDAO.count(map);
        return new MutablePair<>(count, list);
    }
}
