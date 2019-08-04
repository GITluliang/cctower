package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.TopUpRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-04-27 00:52
 */
@Repository
public interface TopUpRecordDAO {
    int deleteByPrimaryKey(Long id);

    int insert(TopUpRecord record);

    int insertSelective(TopUpRecord record);

    TopUpRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TopUpRecord record);

    int updateByPrimaryKey(TopUpRecord record);

    TopUpRecord findByOrderSn(String orderSn);

    List<TopUpRecord> findByOpenId(Map<String, Object> map);
}