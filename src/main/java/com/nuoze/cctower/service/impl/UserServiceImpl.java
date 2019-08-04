package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.MD5Utils;
import com.nuoze.cctower.dao.BusinessTransactionRecordDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.UserDAO;
import com.nuoze.cctower.dao.UserRoleDAO;
import com.nuoze.cctower.pojo.entity.BusinessTransactionRecord;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<User> list(Map<String, Object> map) {
        return userDAO.list(map);
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
        user.setBalance(EMPTY_MONEY);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int count = userDAO.insert(user);
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
        insertUserRole(vo);
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
        Pair<Integer, List<TenantTopUpVO>> pair = new MutablePair<>(count, new ArrayList<>());
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                Long parkingId = user.getParkingId();
                String parkingName = parkingDAO.selectByPrimaryKey(parkingId).getName();
                String businessName = user.getName();
                TenantTopUpVO vo = new TenantTopUpVO();
                vo.setUserId(user.getId());
                vo.setParkingName(parkingName);
                vo.setBusinessName(businessName);
                vo.setBalance(user.getBalance());
                pair.getRight().add(vo);
            }
        }
        return pair;
    }

    @Override
    public int updateBalance(TenantTopUpVO vo) {
        User user = userDAO.selectByPrimaryKey(vo.getUserId());
        BigDecimal balance = user.getBalance().add(vo.getBalance());
        BusinessTransactionRecord businessTransactionRecord = new BusinessTransactionRecord();
        businessTransactionRecord.setAmount(vo.getBalance());
        businessTransactionRecord.setType(1);
        businessTransactionRecord.setUserId(vo.getUserId());
        businessTransactionRecord.setCreateTime(new Date());
        businessTransactionRecord.setBalance(balance);
        businessTransactionRecordDAO.insert(businessTransactionRecord);
        user.setBalance(balance);
        user.setUpdateTime(new Date());
        return userDAO.updateByPrimaryKeySelective(user);
    }

    private void insertUserRole(UserVO vo) {
        List<Long> roleIds = vo.getRoleIds();
        userRoleDAO.removeByUserId(vo.getId());
        List<UserRole> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(vo.getId());
            userRole.setRoleId(roleId);
            list.add(userRole);
        }
        if (!list.isEmpty()) {
            userRoleDAO.batchSave(list);
        }
    }
}
