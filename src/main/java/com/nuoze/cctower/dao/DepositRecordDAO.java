package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.DepositRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-10 14:26
 */
@Repository
public interface DepositRecordDAO extends BaseDAO<DepositRecord> {

    List<DepositRecord> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int batchRemove(Long[] ids);
}