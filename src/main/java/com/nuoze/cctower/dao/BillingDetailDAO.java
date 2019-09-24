package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.BillingDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * long-long-car
 *
 * @author JiaShun
 */
@Repository
public interface BillingDetailDAO extends BaseDAO<BillingDetail> {
    int batchRemove(Long[] ids);

    List<BillingDetail> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    BillingDetail selectByParkingId(Long parkingId);

}