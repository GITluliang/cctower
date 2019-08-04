package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Billing;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * long-long-car
 * @author JiaShun
 */

@Repository
public interface BillingDAO extends BaseDAO<Billing> {
    int batchRemove(Long[] ids);

    List<Billing> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    Billing selectByParkingId(Long parkingId);
}