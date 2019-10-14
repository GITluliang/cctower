package com.nuoze.cctower.dao;

import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.pojo.entity.Passageway;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface PassagewayDAO extends BaseDAO<Passageway> {

    List<Passageway> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int batchRemove(Long[] ids);

    /**
     * 通过停车场id和出入口摄像头IP，查找到车场通道信息
     * @param parkingId     停车场id
     * @param ip            出入口摄像头IP
     * @return Passageway   车场通道
     */
    Passageway findByParkingIdAndIp(@Param("parkingId") Long parkingId, @Param("ip") String ip);

    void deleteByParkingIdAndIp(@Param("parkingId") Long parkingId, @Param("ip") String ip);
}