package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.RoleDAO;
import com.nuoze.cctower.dao.UserDAO;
import com.nuoze.cctower.dao.UserRoleDAO;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.Role;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.pojo.entity.UserRole;
import com.nuoze.cctower.pojo.vo.UserVO;
import com.nuoze.cctower.service.BusinessService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static com.nuoze.cctower.common.constant.Constant.EMPTY_LIST;

/**
 * @author JiaShun
 * @date 2019-03-13 23:19
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    private static final String BUSINESS_ROLE_NAME = "商户";

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ParkingDAO parkingDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private IdComponent idComponent;

    @Override
    public List<UserVO> list(Map<String, Object> map) {
        map.put("type", 1);
        List<User> list = userDAO.list(map);
        List<UserVO> userVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                Long parkingId = user.getParkingId();
                Parking parking = parkingDAO.selectByPrimaryKey(parkingId);
                if(parking != null) {
                    userVO.setParkingName(parking.getName());
                }
                userVOS.add(userVO);
            }
        }
        return userVOS;
    }

    @Override
    public int count(Map<String, Object> map) {
        map.put("type", 1);
        return userDAO.count(map);
    }

    @Override
    public int save(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int count = userDAO.insertSelective(user);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        vo.setId(userDAO.findByUsername(user.getUsername()).getId());
        insertUserRole(vo);
        return count;
    }

    private void insertUserRole(UserVO vo) {
        userRoleDAO.removeByUserId(vo.getId());
        Role role = roleDAO.findByRoleName(BUSINESS_ROLE_NAME);
        UserRole userRole = new UserRole();
        userRole.setUserId(vo.getId());
        userRole.setRoleId(role.getId());
        userRoleDAO.insert(userRole);
    }
}
