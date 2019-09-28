package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.PaymentComponent;
import com.nuoze.cctower.dao.*;
import com.nuoze.cctower.pojo.dto.DepositRecordDTO;
import com.nuoze.cctower.pojo.entity.Account;
import com.nuoze.cctower.pojo.entity.DepositRecord;
import com.nuoze.cctower.pojo.entity.ParkingTradingRecord;
import com.nuoze.cctower.pojo.vo.DepositRecordVO;
import com.nuoze.cctower.service.DepositRecordService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.nuoze.cctower.common.constant.Constant.*;

/**
 * @author JiaShun
 * @date 2019-03-10 13:31
 */

@Service
public class DepositRecordServiceImpl implements DepositRecordService {
    @Autowired
    private DepositRecordDAO depositRecordDAO;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ParkingDAO parkingDAO;
    @Autowired
    private ParkingTradingRecordDAO tradingRecordDAO;
    @Autowired
    private PaymentComponent paymentComponent;

    @Override
    public DepositRecord get(Long id) {
        return depositRecordDAO.selectByPrimaryKey(id);
    }

    @Override
    public List<DepositRecordVO> list(Map<String, Object> map) {
        List<DepositRecord> list = depositRecordDAO.list(map);
        List<DepositRecordVO> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (DepositRecord depositRecord : list) {
                DepositRecordVO vo = new DepositRecordVO();
                BeanUtils.copyProperties(depositRecord, vo);
                Long userId = depositRecord.getUserId();
                Long parkingId = depositRecord.getParkingId();
                if (userId != null) {
                    String userName = userDAO.selectByPrimaryKey(userId).getUsername();
                    vo.setUserName(userName);
                }
                if (parkingId != null) {
                    String parkingName = parkingDAO.selectByPrimaryKey(parkingId).getName();
                    vo.setParkingName(parkingName);
                }
                vos.add(vo);
            }
        }
        return vos;
    }

    @Override
    public int count(Map<String, Object> map) {
        return depositRecordDAO.count(map);
    }

    @Override
    public int save(DepositRecordDTO depositRecordDTO) {
        DepositRecord depositRecord = new DepositRecord();
        BeanUtils.copyProperties(depositRecordDTO, depositRecord);
        Long userId = ShiroUtils.getUserId();
        depositRecord.setUserId(userId);
        depositRecord.setStatus(0);
        depositRecord.setCreateTime(new Date());
        BigDecimal amount = depositRecord.getAmount();
        Account account = accountDAO.selectByPrimaryKey(depositRecord.getAccountId());
        BigDecimal serviceCharge = paymentComponent.getServiceCharge(amount, account.getServiceCharge());
        depositRecord.setServiceCharge(serviceCharge);
        BigDecimal balance = account.getBalance().subtract(amount.add(serviceCharge)).setScale(2, 1);
        if (balance.compareTo(EMPTY_MONEY) <= 0) {
            balance = EMPTY_MONEY;
            amount = account.getBalance().subtract(serviceCharge);
            depositRecord.setAmount(amount);
        }
        //更新交易记录
        ParkingTradingRecord tradingRecord = new ParkingTradingRecord();
        tradingRecord.setParkingId(depositRecord.getParkingId());
        tradingRecord.setType(PARKING_TRADING_RECORD_EXPEND_TYPE);
        tradingRecord.setAmount(amount.add(serviceCharge));
        tradingRecord.setPayTime(new Date());
        tradingRecordDAO.insert(tradingRecord);
        //停车场余额更新
        account.setBalance(balance);
        accountDAO.updateByPrimaryKeySelective(account);
        return depositRecordDAO.insert(depositRecord);
    }

    @Override
    public int update(DepositRecord depositRecord) {
        return depositRecordDAO.updateByPrimaryKeySelective(depositRecord);
    }

    @Override
    public int remove(Long id) {
        return depositRecordDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return depositRecordDAO.batchRemove(ids);
    }

    @Override
    public int finishDeposit(Long id) {
        DepositRecord depositRecord = depositRecordDAO.selectByPrimaryKey(id);
        depositRecord.setStatus(1);
        depositRecord.setTransferTime(new Date());
        return depositRecordDAO.updateByPrimaryKeySelective(depositRecord);
    }

}
