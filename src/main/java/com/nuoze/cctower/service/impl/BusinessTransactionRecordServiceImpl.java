package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.dao.BusinessTransactionRecordDAO;
import com.nuoze.cctower.dao.UserDAO;
import com.nuoze.cctower.pojo.entity.BusinessTransactionRecord;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.pojo.vo.BusinessTransactionRecordVO;
import com.nuoze.cctower.service.BusinessTransactionRecordService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-05-05 01:29
 */
@Service
public class BusinessTransactionRecordServiceImpl implements BusinessTransactionRecordService {

    @Autowired
    private BusinessTransactionRecordDAO businessTransactionRecordDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public Pair<Integer, List<BusinessTransactionRecordVO>> list(Map<String, Object> map) {
        List<BusinessTransactionRecordVO> voList = new ArrayList<>();
        List<BusinessTransactionRecord> list = businessTransactionRecordDAO.list(map);
        if (!CollectionUtils.isEmpty(list)) {
            for (BusinessTransactionRecord btr : list) {
                BusinessTransactionRecordVO vo = new BusinessTransactionRecordVO();
                BeanUtils.copyProperties(btr, vo);
                vo.setCreateTimeStr(DateUtils.formatDateTime(btr.getCreateTime()));
                User user = userDAO.selectByPrimaryKey(btr.getUserId());
                if (user != null) {
                    vo.setBusinessName(user.getName());
                }
                voList.add(vo);
            }
        }
        int count = businessTransactionRecordDAO.count(map);
        return new MutablePair<>(count, voList);
    }
}
