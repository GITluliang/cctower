package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.entity.Role;
import com.nuoze.cctower.pojo.vo.RoleVO;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-03 16:57
 */
public interface RoleService {

    /**
     * 获取角色列表
     *
     * @return List<Role>
     */
    List<Role> list();

    /**
     * 根据userId获取List<Role>
     *
     * @param userId userId
     * @return List<Role>
     */
    List<Role> list(Long userId);

    /**
     * 根据id获取Role
     *
     * @param id roleId
     * @return Role
     */
    Role findById(Long id);

    /**
     * 保存Role
     *
     * @param vo role
     * @return count
     */
    int save(RoleVO vo);

    /**
     * 更新Role
     *
     * @param vo 角色
     * @return count
     */
    int update(RoleVO vo);

    /**
     * 删除Role
     *
     * @param id roleId
     * @return count
     */
    int remove(Long id);

    /**
     * 批量删除Role
     *
     * @param ids [ids]
     * @return count
     */
    int batchRemove(Long[] ids);

    /**
     * 检验是否重复roleName
     *
     * @param params 条件
     * @return boolean
     */
    boolean exit(Map<String, Object> params);
}
