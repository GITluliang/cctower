package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-01-18 23:35
 */

@Repository
public interface AccountDAO extends BaseDAO<Account> {

    List<Account> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int batchRemove(Long[] ids);

    Account selectByParkingId(Long parkingId);

    int deleteByParkingId(Long id);
}