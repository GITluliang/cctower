package com.nuoze.cctower.service;

import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.pojo.dto.BillingDetailDTO;
import com.nuoze.cctower.pojo.entity.BillingDetail;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-07-03 23:28
 */
public interface BillingDetailService {

    List<BillingDetailDTO> list(Query query);

    int count(Query query);

    BillingDetail findById(Long id);

    int save(BillingDetail billingDetail);

    int update(BillingDetail billingDetail);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
