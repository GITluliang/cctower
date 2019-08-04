package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.MemberCar;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-01-18 23:35
 */
@Repository
public interface MemberCarDAO extends BaseDAO<MemberCar> {

    List<Long> selectCarIdByMemberId(Long memberId);

    List<MemberCar> selectByCarId(Long carId);
}