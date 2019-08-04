package com.nuoze.cctower.component;

import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.UserRoleDAO;
import com.nuoze.cctower.pojo.entity.Parking;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.nuoze.cctower.common.constant.Constant.*;

/**
 * @author JiaShun
 * @date 2019-03-17 23:05
 */
@Component
public class IdComponent {
    @Autowired
    private ParkingDAO parkingDAO;
    @Autowired
    private UserRoleDAO userRoleDAO;

    /**
     * 当返回null代表角色是管理员，可以查看所有。
     * 当返回[]代表是普通角色，并且没有拥有的parking。
     * @return List
     */
    public List<Parking> getParkingList() {
        List<Parking> parkingList = parkingDAO.list(null);
        Long userId = ShiroUtils.getUser().getId();
        List<Long> roleIds = userRoleDAO.listRoleByUserId(userId);
        if (!roleIds.contains(SUPER_ROLE_ID) && !roleIds.contains(ADMIN_ROLE_ID)) {
            parkingList = parkingDAO.findParkingByUser(userId);
            if (CollectionUtils.isEmpty(parkingList)) {
                return new ArrayList<>(0);
            }
        }
        return parkingList;
    }

    public boolean isNotAdmin(Long userId) {
        List<Long> roleIds = userRoleDAO.listRoleByUserId(userId);
        return !roleIds.contains(SUPER_ROLE_ID) && !roleIds.contains(ADMIN_ROLE_ID);
    }

    public List<Long> getParkingIds() {
        List<Long> parkingIds = null;
        Long userId = ShiroUtils.getUser().getId();
        List<Long> roleIds = userRoleDAO.listRoleByUserId(userId);
        if (!roleIds.contains(SUPER_ROLE_ID) && !roleIds.contains(ADMIN_ROLE_ID)) {
            parkingIds = parkingDAO.findByUserId(userId);
            if (CollectionUtils.isEmpty(parkingIds)) {
                return new ArrayList<>(0);
            }
        }
        return parkingIds;
    }

    public Long getUserId() {
        Long userId = ShiroUtils.getUser().getId();
        List<Long> roleIds = userRoleDAO.listRoleByUserId(userId);
        if (roleIds.contains(SUPER_ROLE_ID) || roleIds.contains(ADMIN_ROLE_ID)) {
            return null;
        }
        return userId;
    }

    public Map<String, Object> buildParams(Map<String, Object> params) {
        List<Long> parkingIds = this.getParkingIds();
        //如果角色是管理员，不处理params，代表所有parkingIds
        if (parkingIds == null) {
            return params;
        }
        //如果不是管理员，拥有的parkingIds还为空，返回空map
        if (parkingIds.isEmpty()) {
            params.clear();
        } else {
            params.put("parkingIds", parkingIds);
        }
        return params;
    }
}
