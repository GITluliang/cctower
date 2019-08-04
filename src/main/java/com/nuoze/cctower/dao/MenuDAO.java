package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-01-18 23:35
 */
@Repository
public interface MenuDAO extends BaseDAO<Menu> {

    /**
     * 获取对应的shiro权限perms
     * @param userId userId
     * @return perms
     */
    List<String> listUserPerms(Long userId);

    /**
     * 获取对应的菜单
     * @param userId userId
     * @return menus
     */
    List<Menu> listMenuByUserId(Long userId);

    /**
     * 获取MENU列表
     * @param params 条件
     * @return List<Menu>
     */
    List<Menu> list(Map<String, Object> params);

}