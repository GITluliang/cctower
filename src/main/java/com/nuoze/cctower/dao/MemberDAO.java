package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *  ToC用户表
 * @author JiaShun
 * @date 2019-03-01 01:57
 */
@Repository
public interface MemberDAO extends BaseDAO<Member> {
    Member selectByOpenId(String openId);

    /**
     * 通过id进行查询
     * @param id
     * @return
     */
    Member get(Long id);

    /**
     * 查询所有
     * @param map
     * @return
     */
    List<Member> list(Map<String,Object> map);

    /**
     * 统计个数
     * @param map
     * @return
     */
    int count(Map<String,Object> map);
}