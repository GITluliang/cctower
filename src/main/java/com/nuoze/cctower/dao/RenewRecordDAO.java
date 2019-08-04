package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.RenewRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface RenewRecordDAO extends BaseDAO<RenewRecord> {

    RenewRecord findByOrderSn(String orderSn);

    List<RenewRecord> findByOpenId(Map<String, Object> map);

    List<RenewRecord> listByParkingId(Map<String, Object> map);

    int countByParkingId(Map<String, Object> map);
}