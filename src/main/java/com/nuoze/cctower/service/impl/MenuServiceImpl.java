package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.domain.Tree;
import com.nuoze.cctower.common.util.BuildTree;
import com.nuoze.cctower.dao.MenuDAO;
import com.nuoze.cctower.dao.RoleMenuDAO;
import com.nuoze.cctower.pojo.entity.Menu;
import com.nuoze.cctower.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.nuoze.cctower.common.constant.Constant.MAP_INIT_CAPACITY;

/**
 * @author JiaShun
 * @date 2019-02-28 23:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private RoleMenuDAO roleMenuDAO;

    @Override
    public Set<String> listPerms(Long userId) {
        List<String> perms = menuDAO.listUserPerms(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNoneBlank(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<Tree<Menu>> listMenuTree(Long userId) {
        List<Tree<Menu>> trees = new CopyOnWriteArrayList<>();
        List<Menu> menus = menuDAO.listMenuByUserId(userId);
        for (Menu menu : menus) {
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getName());
            Map<String, Object> attributes = new HashMap<>(MAP_INIT_CAPACITY);
            attributes.put("url", menu.getUrl());
            attributes.put("icon", menu.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        return BuildTree.buildList(trees, "0");
    }

    @Override
    public List<Menu> list(Map<String, Object> params) {
        return menuDAO.list(params);
    }

    @Override
    public Menu findById(Long id) {
        return menuDAO.selectByPrimaryKey(id);
    }

    @Override
    public int save(Menu menu) {
        menu.setCreateTime(new Date());
        menu.setUpdateTime(new Date());
        return menuDAO.insert(menu);
    }

    @Override
    public int update(Menu menu) {
        menu.setUpdateTime(new Date());
        return menuDAO.updateByPrimaryKeySelective(menu);
    }

    @Override
    public int remove(Long id) {
        return menuDAO.deleteByPrimaryKey(id);
    }

    @Override
    public Tree<Menu> getTree(Long roleId) {
        List<Tree<Menu>> trees = new CopyOnWriteArrayList<>();
        List<Menu> menus = menuDAO.list(new HashMap<>(MAP_INIT_CAPACITY));
        if (null == roleId) {
            for (Menu menu : menus) {
                Tree<Menu> tree = buildTree(menu);
                trees.add(tree);
            }
        } else {
            // 根据roleId查询权限
            List<Long> menuIds = roleMenuDAO.listMenuIdByRoleId(roleId);
            for (Menu menu : menus) {
                menuIds.remove(menu.getParentId());
            }
            List<Menu> menuList = menuDAO.list(new HashMap<>(MAP_INIT_CAPACITY));
            for (Menu menu : menuList) {
                Tree<Menu> tree = buildTree(menu);
                Map<String, Object> state = new HashMap<>(MAP_INIT_CAPACITY);
                Long menuId = menu.getId();
                if (menuIds.contains(menuId)) {
                    state.put("selected", true);
                } else {
                    state.put("selected", false);
                }
                tree.setState(state);
                trees.add(tree);
            }
        }
        return BuildTree.build(trees);
    }

    private Tree<Menu> buildTree(Menu menu) {
        Tree<Menu> tree = new Tree<>();
        tree.setId(menu.getId().toString());
        tree.setParentId(menu.getParentId().toString());
        tree.setText(menu.getName());
        return tree;
    }


}
