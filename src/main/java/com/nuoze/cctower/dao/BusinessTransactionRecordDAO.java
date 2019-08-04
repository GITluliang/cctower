package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.BusinessTransactionRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-01-18 23:35
 */

@Repository
public interface BusinessTransactionRecordDAO extends BaseDAO<BusinessTransactionRecord> {

    List<BusinessTransactionRecord> list(Map<String, Object> map);

    int count(Map<String, Object> map);
}