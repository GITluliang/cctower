package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.RechargeRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author luliang
 * @Date 2019-12-04 11:36
 */
@Repository
public interface RechargeRecordDAO extends BaseDAO<RechargeRecord> {

    RechargeRecord findByOrderSn(String orderSn);

    List<RechargeRecord> list(Map<String, Object> map);
}
