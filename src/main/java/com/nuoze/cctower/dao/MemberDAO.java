package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Member;
import org.springframework.stereotype.Repository;

/**
 * @author JiaShun
 * @date 2019-03-01 01:57
 */
@Repository
public interface MemberDAO extends BaseDAO<Member> {
    Member selectByOpenId(String openId);
}