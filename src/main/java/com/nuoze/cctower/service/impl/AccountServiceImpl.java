package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.dao.AccountDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.ParkingRecordDAO;
import com.nuoze.cctower.pojo.entity.Account;
import com.nuoze.cctower.pojo.entity.ParkingRecord;
import com.nuoze.cctower.pojo.vo.AccountVO;
import com.nuoze.cctower.service.AccountService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.EMPTY_MONEY;

/**
 * @author JiaShun
 * @date 2019-03-01 01:57
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private ParkingRecordDAO parkingRecordDAO;
    @Autowired
    private ParkingDAO parkingDAO;
    @Autowired
    private PaymentComponent paymentComponent;

    @Override
    public Account findByParkingId(Long parkingId) {
        return accountDAO.selectByParkingId(parkingId);
    }

    @Override
    public Account get(Long id) {
        return accountDAO.selectByPrimaryKey(id);
    }

    @Override
    public List<AccountVO> list(Map<String, Object> map) {
        List<Account> list = accountDAO.list(map);
        List<AccountVO> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (Account account : list) {
                AccountVO vo = new AccountVO();
                BeanUtils.copyProperties(account, vo);
                Long parkingId = account.getParkingId();
                String parkingName = parkingDAO.selectByPrimaryKey(parkingId).getName();
                if (StringUtils.isNotBlank(parkingName)) {
                    vo.setParkingName(parkingName);
                }
                BigDecimal withdrawalAmount = paymentComponent.balanceToWithdrawalAmount(account.getBalance(), account.getServiceCharge());
                vo.setWithdrawalAmount(withdrawalAmount);
                vos.add(vo);
            }
        }
        return vos;
    }

    @Override
    public int count(Map<String, Object> map) {
        return accountDAO.count(map);
    }

    @Override
    public int save(Account account) {
        Long parkingId = account.getParkingId();
        List<ParkingRecord> list = parkingRecordDAO.findByParkingId(parkingId);
        BigDecimal balance = EMPTY_MONEY;
        if (!CollectionUtils.isEmpty(list)) {
            for (ParkingRecord record : list) {
                BigDecimal cost = record.getCost();
                if (cost != null) {
                    balance = balance.add(cost);
                }
            }
        }
        account.setBalance(balance);
        account.setCreateTime(new Date());
        return accountDAO.insert(account);
    }

    @Override
    public int update(Account account) {
        return accountDAO.updateByPrimaryKeySelective(account);
    }

    @Override
    public int remove(Long id) {
        return accountDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return accountDAO.batchRemove(ids);
    }

}
