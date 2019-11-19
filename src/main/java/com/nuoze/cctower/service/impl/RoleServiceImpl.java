package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.dao.RoleDAO;
import com.nuoze.cctower.dao.RoleMenuDAO;
import com.nuoze.cctower.dao.UserRoleDAO;
import com.nuoze.cctower.pojo.entity.Role;
import com.nuoze.cctower.pojo.entity.RoleMenu;
import com.nuoze.cctower.pojo.vo.RoleVO;
import com.nuoze.cctower.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.nuoze.cctower.common.constant.Constant.MAP_INIT_CAPACITY;

/**
 * @author JiaShun
 * @date 2019-03-03 16:57
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserRoleDAO userRoleDAO;
    @Autowired
    private RoleMenuDAO roleMenuDAO;

    @Override
    public List<Role> list() {
        List<Role> list = roleDAO.list(new HashMap<>(MAP_INIT_CAPACITY));
        Long userId = ShiroUtils.getUserId();
        if (userId != 1) {
            list.removeIf(role -> role.getId() == 1);
        }
        return list;
    }

    @Override
    public List<Role> list(Long userId) {
        List<Long> roleIds = userRoleDAO.listRoleByUserId(userId);
        List<Role> roles = roleDAO.list(new HashMap<>(MAP_INIT_CAPACITY));
        for (Role role : roles) {
            role.setSign("false");
            for (Long roleId : roleIds) {
                if (roleId.equals(role.getId())) {
                    role.setSign("true");
                    break;
                }
            }
        }
        return roles;
    }

    @Override
    public Role findById(Long id) {
        return roleDAO.selectByPrimaryKey(id);
    }

    @Override
    public int save(RoleVO vo) {
        Role role = new Role();
        BeanUtils.copyProperties(vo, role);
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        int count = roleDAO.insert(role);
        vo.setId(roleDAO.findByRoleName(vo.getName()).getId());
        saveRoleMenu(vo);
        return count;
    }

    @Override
    public int update(RoleVO vo) {
        Role role = new Role();
        BeanUtils.copyProperties(vo, role);
        role.setUpdateTime(new Date());
        int count = roleDAO.updateByPrimaryKeySelective(role);
        saveRoleMenu(vo);
        return count;
    }

    @Override
    public int remove(Long id) {
        int count = roleDAO.deleteByPrimaryKey(id);
        userRoleDAO.removeByRoleId(id);
        roleMenuDAO.removeByRoleId(id);
        return count;
    }

    @Override
    public int batchRemove(Long[] ids) {
        return roleDAO.batchRemove(ids);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        return roleDAO.list(params).size() > 0;
    }

    private void saveRoleMenu(RoleVO vo) {
        List<Long> menuIds = vo.getMenuIds();
        Long roleId = vo.getId();
        List<RoleMenu> rms = new CopyOnWriteArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            rms.add(rm);
        }
        roleMenuDAO.removeByRoleId(roleId);
        if (!rms.isEmpty()) {
            roleMenuDAO.batchSave(rms);
        }
    }
}
