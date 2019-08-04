package com.nuoze.cctower.service.applet;

import com.nuoze.cctower.pojo.entity.TopUpRecord;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-04-10 01:45
 */
public interface TopUpRecordService {
    int save(TopUpRecord topUpRecord);

    TopUpRecord findByOrderSn(String orderSn);

    int update(TopUpRecord topUpRecord);

    List<TopUpRecord> findByOpenId(Map<String, Object> map);
}
