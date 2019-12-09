package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.MD5Utils;
import com.nuoze.cctower.dao.*;
import com.nuoze.cctower.pojo.entity.BusinessTransactionRecord;
import com.nuoze.cctower.pojo.entity.Role;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.pojo.entity.UserRole;
import com.nuoze.cctower.pojo.vo.TenantTopUpVO;
import com.nuoze.cctower.pojo.vo.UserVO;
import com.nuoze.cctower.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;
import static com.nuoze.cctower.common.util.ShiroUtils.getUser;

/**
 * @author JiaShun
 * @date 2019-03-03 15:46
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static final String ADMIN_NAME = "admin";

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserRoleDAO userRoleDAO;
    @Autowired
    private ParkingDAO parkingDAO;
    @Autowired
    private BusinessTransactionRecordDAO businessTransactionRecordDAO;
    @Autowired
    private RoleDAO roleDAO;

    @Override
    public List<UserVO> list(Map<String, Object> map) {
        List<UserVO> userVOList = new CopyOnWriteArrayList<>();
        List<User> list = userDAO.list(map);
        for (User user : list) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            List<Role> roleList = new CopyOnWriteArrayList<>();
            for (Long roleId : userRoleDAO.listRoleByUserId(user.getId())) {
                roleList.add(roleDAO.selectByPrimaryKey(roleId));
            }
            userVO.setRoleList(roleList);
            userVOList.add(userVO);
        }
        return userVOList;
    }

    @Override
    public UserVO findByIdVO(Long id) {
        User user = userDAO.selectByPrimaryKey(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO.setParkingName(parkingDAO.selectByPrimaryKey(user.getParkingId()).getName());
    }

    @Override
    public int count(Map<String, Object> map) {
        return userDAO.count(map);
    }

    @Override
    public User findById(Long id) {
        return userDAO.selectByPrimaryKey(id);
    }

    @Override
    public int save(UserVO vo) {
        User user = new User();
        BeanUtils.copyProperties(vo, user);
        int count = userDAO.insert(user.setBalance(EMPTY_MONEY).setCreateTime(new Date()).setUpdateTime(new Date()));
        vo.setId(userDAO.findByUsername(vo.getUsername()).getId());
        insertUserRole(vo);
        return count;
    }

    @Override
    public int update(UserVO vo) {
        User user = new User();
        BeanUtils.copyProperties(vo, user);
        user.setUpdateTime(new Date());
        int count = userDAO.updateByPrimaryKeySelective(user);
        if (vo.getRoleIds() != null) {
            insertUserRole(vo);
        }
        return count;
    }

    @Override
    public int remove(Long id) {
        userRoleDAO.removeByUserId(id);
        return userDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        int count = userDAO.batchRemove(ids);
        userRoleDAO.batchRemoveByUserId(ids);
        return count;
    }

    @Override
    public int updatePersonal(User user) {
        user.setUpdateTime(new Date());
        return userDAO.updateByPrimaryKeySelective(user);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        return userDAO.list(params).size() > 0;
    }

    @Override
    public int resetPwd(UserVO vo) throws Exception {
        User user = getUser();
        if (user.getId().equals(vo.getId())) {
            if (MD5Utils.encrypt(user.getUsername(), vo.getOldPwd()).equals(user.getPassword())) {
                user.setPassword(MD5Utils.encrypt(user.getUsername(), vo.getNewPwd()));
                user.setUpdateTime(new Date());
                return userDAO.updateByPrimaryKeySelective(user);
            } else {
                throw new Exception("输入的旧密码有误！");
            }
        } else {
            throw new Exception("你修改的不是你登录的账号");
        }
    }

    @Override
    public int adminResetPwd(UserVO vo) throws Exception {
        User user = findById(vo.getId());
        if (ADMIN_NAME.equals(user.getUsername())) {
            throw new Exception("超级管理员的账号不允许直接重置！");
        }
        user.setPassword(MD5Utils.encrypt(user.getUsername(), vo.getNewPwd()));
        user.setUpdateTime(new Date());
        return userDAO.updateByPrimaryKeySelective(user);
    }

    @Override
    public Pair<Integer, List<TenantTopUpVO>> tenantTopUpList(Map<String, Object> map) {
        List<User> list = userDAO.list(map);
        int count = userDAO.count(map);
        Pair<Integer, List<TenantTopUpVO>> pair = new MutablePair<>(count, new CopyOnWriteArrayList<>());
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                pair.getRight().add(new TenantTopUpVO().setUserId(user.getId()).setParkingName(parkingDAO.selectByPrimaryKey(user.getParkingId()).getName()).setBusinessName(user.getName()).setBalance(user.getBalance()).setTimeCoupon(user.getTimeCoupon()));
            }
        }
        return pair;
    }

    @Override
    public int updateBalance(TenantTopUpVO vo) {
        User user = userDAO.selectByPrimaryKey(vo.getUserId());
        BigDecimal balance = user.getBalance().add(vo.getBalance());
        businessTransactionRecordDAO.insert(new BusinessTransactionRecord().setAmount(vo.getBalance()).setType(1).setUserId(vo.getUserId()).setCreateTime(new Date()).setBalance(balance).setStatus(0).setParkingId(user.getParkingId()));
        return userDAO.updateByPrimaryKeySelective(user.setBalance(balance).setUpdateTime(new Date()));
    }

    @Override
    public int updateTimeCoupon(TenantTopUpVO vo) {
        User user = userDAO.selectByPrimaryKey(vo.getUserId());
        Integer timeCoupon = user.getTimeCoupon() != null ? (user.getTimeCoupon() + vo.getTimeCoupon()) : vo.getTimeCoupon();
        businessTransactionRecordDAO.insertSelective(new BusinessTransactionRecord().setAmount(BigDecimal.valueOf(vo.getTimeCoupon())).setBalance(BigDecimal.valueOf(timeCoupon)).setType(1).setUserId(vo.getUserId()).setCreateTime(new Date()).setStatus(1).setParkingId(user.getParkingId()));
        return userDAO.updateByPrimaryKeySelective(user.setTimeCoupon(timeCoupon).setUpdateTime(new Date()));
    }

    private void insertUserRole(UserVO vo) {
        List<Long> roleIds = vo.getRoleIds();
        userRoleDAO.removeByUserId(vo.getId());
        List<UserRole> list = new CopyOnWriteArrayList<>();
        for (Long roleId : roleIds) {
            list.add(new UserRole().setUserId(vo.getId()).setRoleId(roleId));
        }
        if (!list.isEmpty()) {
            userRoleDAO.batchSave(list);
        }
    }
}
