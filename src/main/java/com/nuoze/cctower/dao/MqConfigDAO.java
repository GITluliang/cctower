package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.MqConfig;
import org.springframework.stereotype.Repository;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface MqConfigDAO extends BaseDAO<MqConfig> {
    /**
     * 通过停车场查询mq队列名
     * @param parkingId
     * @return
     */
    String selectQueueByParkingId(Long parkingId);
}