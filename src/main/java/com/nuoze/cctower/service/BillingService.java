package com.nuoze.cctower.service;

import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.pojo.dto.BillingDTO;
import com.nuoze.cctower.pojo.entity.Billing;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-31 10:52
 */
public interface BillingService {
    List<BillingDTO> list(Query query);

    int count(Query query);

    Billing findById(Long id);

    int save(Billing billing);

    int update(Billing billing);

    int remove(Long id);

    int batchRemove(Long[] ids);

    Billing findByParkingId(Long parkingId);
}
