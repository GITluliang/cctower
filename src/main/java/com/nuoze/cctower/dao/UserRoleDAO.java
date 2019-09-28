package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface UserRoleDAO extends BaseDAO<UserRole> {

    /**
     * 删除用户和角色的对应关系
     *
     * @param userIds [userId]
     */
    void batchRemoveByUserId(Long[] userIds);

    /**
     * 根据用户ID获取对应的RoleIds
     *
     * @param userId userId
     * @return List<RoleId>
     */
    List<Long> listRoleByUserId(Long userId);

    /**
     * 根据userId删除角色
     *
     * @param userId [userId]
     * @return int
     */
    int removeByUserId(Long userId);

    /**
     * 批量添加UserRole
     *
     * @param list List<UserRole>
     * @return num
     */
    int batchSave(List<UserRole> list);

    /**
     * 根据roleId删除userRole
     *
     * @param roleId
     */
    void removeByRoleId(Long roleId);
}