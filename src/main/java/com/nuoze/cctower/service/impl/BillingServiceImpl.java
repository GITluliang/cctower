package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.dao.BillingDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.dto.BillingDTO;
import com.nuoze.cctower.pojo.entity.Billing;
import com.nuoze.cctower.service.BillingService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-31 10:52
 */
@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingDAO billingDAO;
    @Autowired
    private ParkingDAO parkingDAO;

    @Override
    public List<BillingDTO> list(Query query) {
        List<Billing> list = billingDAO.list(query);
        List<BillingDTO> dtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Billing billing : list) {
                BillingDTO dto = new BillingDTO();
                BeanUtils.copyProperties(billing, dto);
                String parkingName = parkingDAO.selectByPrimaryKey(dto.getParkingId()).getName();
                dto.setParkingName(parkingName);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public int count(Query query) {
        return billingDAO.count(query);
    }

    @Override
    public Billing findById(Long id) {
        return billingDAO.selectByPrimaryKey(id);
    }

    @Override
    public int save(Billing billing) {
        return billingDAO.insert(billing);
    }

    @Override
    public int update(Billing billing) {
        return billingDAO.updateByPrimaryKey(billing);
    }

    @Override
    public int remove(Long id) {
        return billingDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return billingDAO.batchRemove(ids);
    }

    @Override
    public Billing findByParkingId(Long parkingId) {
        return billingDAO.selectByParkingId(parkingId);
    }
}
