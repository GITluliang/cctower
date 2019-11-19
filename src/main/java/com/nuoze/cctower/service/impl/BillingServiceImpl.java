package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.dao.BillingDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.dto.BillingDTO;
import com.nuoze.cctower.pojo.entity.Billing;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.service.BillingService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    @Autowired
    private IdComponent idComponent;

    @Override
    public List<BillingDTO> list(Query query) {
        Long userId = idComponent.getUserId();
        if (userId != null) {
            query.put("userId", userId);
        }
        List<Billing> list = billingDAO.list(query);
        List<BillingDTO> dtoList = new CopyOnWriteArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Billing billing : list) {
                BillingDTO dto = new BillingDTO();
                BeanUtils.copyProperties(billing, dto);
                Parking parking = parkingDAO.selectByPrimaryKey(dto.getParkingId());
                if (parking != null) {
                    dto.setParkingName(parking.getName());
                }
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
        billing.setUserId(ShiroUtils.getUser().getId());
        return billingDAO.insert(billing);
    }

    @Override
    public int update(Billing billing) {
        billing.setUserId(ShiroUtils.getUser().getId());
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
