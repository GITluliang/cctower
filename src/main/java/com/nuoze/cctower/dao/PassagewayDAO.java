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

    Passageway findByParkingIdAndIp(@Param("parkingId") Long parkingId, @Param("ip")String ip);

    void deleteByParkingIdAndIp(@Param("parkingId") Long parkingId, @Param("ip")String ip);
}