package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.RoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-03 11:11
 */
@Repository
public interface RoleMenuDAO extends BaseDAO<RoleMenu> {

    /**
     * 根据roleId查询菜单权限
     * @param roleId 角色ID
     * @return List<MenuId>
     */
    List<Long> listMenuIdByRoleId(Long roleId);

    /**
     * 根据roleId删除RoleMenu
     * @param roleId roleId
     */
    void removeByRoleId(Long roleId);

    /**
     * 批量添加RoleMenu
     * @param rms List<RoleMenu>
     */
    void batchSave(List<RoleMenu> rms);
}