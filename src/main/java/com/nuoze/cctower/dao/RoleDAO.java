package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface RoleDAO extends BaseDAO<Role> {

    /**
     * 条件检索List
     * @param map 条件
     * @return List<Role>
     */
    List<Role> list(Map<String, Object> map);

    /**
     * 批量删除角色
     * @param ids 角色组
     * @return count
     */
    int batchRemove(Long[] ids);

    /**
     * 根据name查出角色
     * @param name 角色名字
     * @return 角色
     */
    Role findByRoleName(String name);
}