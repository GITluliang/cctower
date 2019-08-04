package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-02-18 23:35
 */
@Repository
public interface UserDAO extends BaseDAO<User> {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return User
     */
    User findByUsername(String username);

    /**
     * 用户列表
     * @param map 条件
     * @return List<User>
     */
    List<User> list(Map<String, Object> map);

    /**
     * 用户总个数
     * @param map 条件
     * @return int
     */
    int count(Map<String, Object> map);

    /**
     * 批量删除
     * @param ids [ids]
     * @return size
     */
    int batchRemove(Long[] ids);
}