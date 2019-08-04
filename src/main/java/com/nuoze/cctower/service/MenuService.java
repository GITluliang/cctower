package com.nuoze.cctower.service;

import com.nuoze.cctower.common.domain.Tree;
import com.nuoze.cctower.pojo.entity.Menu;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author JiaShun
 * @date 2019-02-28 23:57
 */
public interface MenuService {

    /**
     * 根据ID获取对应的
     * @param id
     * @return
     */
    Set<String> listPerms(Long id);

    /**
     * 根据ID获取对应的页面
     * @param userId userId
     * @return Tree: Menu
     */
    List<Tree<Menu>> listMenuTree(Long userId);

    /**
     * 获取菜单列表
     * @param params 条件
     * @return List<Menu>
     */
    List<Menu> list(Map<String, Object> params);

    /**
     * 根据pid获取Menu
     * @param id id
     * @return menu
     */
    Menu findById(Long id);

    /**
     * 保存Menu
     * @param menu entity
     * @return size
     */
    int save(Menu menu);

    /**
     * 更新menu
     * @param menu entity
     * @return size
     */
    int update(Menu menu);

    /**
     * 删除Menu
     * @param id id
     * @return size
     */
    int remove(Long id);

    /**
     * menu tree
     * @param roleId roleId
     * @return Tree<Menu>
     */
    Tree<Menu> getTree(Long roleId);


}
