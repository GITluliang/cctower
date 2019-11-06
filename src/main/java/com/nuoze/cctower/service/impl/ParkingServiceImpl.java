package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.dao.AccountDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.entity.Account;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;

/**
 * @author JiaShun
 * @date 2019-03-11 22:59
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingDAO parkingDAO;

    @Autowired
    private IdComponent idComponent;

    @Autowired
    private AccountDAO accountDAO;

    @Override
    public List<Parking> findParkingByUser(Long userId) {
        return parkingDAO.findParkingByUser(userId);
    }

    @Override
    public List<Parking> list(Map<String, Object> map) {
        Long userId = idComponent.getUserId();
        if (userId != null) {
            map.put("userId", userId);
        }
        return parkingDAO.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return parkingDAO.count(map);
    }

    @Override
    public Parking findById(Long id) {
        return parkingDAO.selectByPrimaryKey(id);
    }

    @Override
    public int save(Parking parking) {
        Long userId = ShiroUtils.getUser().getId();
        parking.setUserId(userId);
        parking.setCreateTime(new Date());
        parking.setUpdateTime(new Date());
        int i = parkingDAO.insertSelective(parking);
        if (i > 0) {
            Account account = new Account();
            account.setParkingId(parkingDAO.findByUserIdAndParkingName(userId, parking.getName()).getId());
            account.setBalance(EMPTY_MONEY);
            account.setCreateTime(new Date());
            account.setServiceCharge(0);
            accountDAO.insert(account);
        }
        return i;
    }

    @Override
    public int update(Parking parking) {
        parking.setUpdateTime(new Date());
        return parkingDAO.updateByPrimaryKeySelective(parking);
    }

    @Override
    public int remove(Long id) {
        accountDAO.deleteByParkingId(id);
        return parkingDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return parkingDAO.batchRemove(ids);
    }
}
