package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.enums.ApiDataEnum;
import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.common.util.ShiroUtils;
import com.nuoze.cctower.component.IdComponent;
import com.nuoze.cctower.component.MqSendComponent;
import com.nuoze.cctower.dao.BillingDAO;
import com.nuoze.cctower.dao.BillingDetailDAO;
import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.pojo.dto.BillingDetailDTO;
import com.nuoze.cctower.pojo.entity.Billing;
import com.nuoze.cctower.pojo.entity.BillingDetail;
import com.nuoze.cctower.service.BillingDetailService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author JiaShun
 * @date 2019-07-03 23:28
 */
@Service
public class BillingDetailServiceImpl implements BillingDetailService {

    @Autowired
    private BillingDetailDAO billingDetailDAO;
    @Autowired
    private BillingDAO billingDAO;
    @Autowired
    private ParkingDAO parkingDAO;
    @Autowired
    private MqSendComponent mqSendComponent;
    @Autowired
    private IdComponent idComponent;

    @Override
    public List<BillingDetailDTO> list(Query query) {
        Long userId = idComponent.getUserId();
        if (userId != null) {
            query.put("userId", userId);
        }
        List<BillingDetail> list = billingDetailDAO.list(query);
        List<BillingDetailDTO> dtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (BillingDetail billing : list) {
                BillingDetailDTO dto = new BillingDetailDTO();
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
        return billingDetailDAO.count(query);
    }

    @Override
    public BillingDetail findById(Long id) {
        return billingDetailDAO.selectByPrimaryKey(id);
    }

    @Override
    public int save(BillingDetail detail) {
        mqSendComponent.sendBilling(ApiDataEnum.ADD, findBillingByDetail(detail), detail);
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        detail.setUserId(ShiroUtils.getUser().getId());
        return billingDetailDAO.insert(detail);
    }

    @Override
    public int update(BillingDetail detail) {
        mqSendComponent.sendBilling(ApiDataEnum.UPDATE, findBillingByDetail(detail), detail);
        detail.setUpdateTime(new Date());
        return billingDetailDAO.updateByPrimaryKeySelective(detail);
    }

    @Override
    public int remove(Long id) {
        mqSendComponent.sendBilling(ApiDataEnum.DELETE, findBillingByDetail(billingDetailDAO.selectByPrimaryKey(id)), billingDetailDAO.selectByPrimaryKey(id));
        return billingDetailDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return billingDetailDAO.batchRemove(ids);
    }


    private Billing findBillingByDetail(BillingDetail detail) {
        Long parkingId = detail.getParkingId();
        return billingDAO.selectByParkingId(parkingId);
    }
}
