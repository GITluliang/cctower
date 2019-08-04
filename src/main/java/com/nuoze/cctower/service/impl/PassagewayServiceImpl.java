package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.dao.ParkingDAO;
import com.nuoze.cctower.dao.PassagewayDAO;
import com.nuoze.cctower.pojo.dto.PassagewayDTO;
import com.nuoze.cctower.pojo.entity.Passageway;
import com.nuoze.cctower.service.PassagewayService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-17 12:17
 */
@Service
public class PassagewayServiceImpl implements PassagewayService {

    @Autowired
    private PassagewayDAO passagewayDAO;
    @Autowired
    private ParkingDAO parkingDAO;

    @Override
    public int count(Map<String, Object> map) {
        return passagewayDAO.count(map);
    }

    @Override
    public List<PassagewayDTO> list(Map<String, Object> map) {
        List<Passageway> list = passagewayDAO.list(map);
        List<PassagewayDTO> passagewayDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (Passageway vo : list) {
                Long parkingId = vo.getParkingId();
                String parkingName = parkingDAO.selectByPrimaryKey(parkingId).getName();
                PassagewayDTO dto = new PassagewayDTO();
                BeanUtils.copyProperties(vo, dto);
                dto.setParkingName(parkingName);
                passagewayDTOS.add(dto);
            }
        }
        return passagewayDTOS;
    }

    @Override
    public Passageway findById(Long id) {
        return passagewayDAO.selectByPrimaryKey(id);
    }

    @Override
    public int save(Passageway passageway) {
        passageway.setCreateTime(new Date());
        passageway.setUpdateTime(new Date());
        return passagewayDAO.insert(passageway);
    }

    @Override
    public int update(Passageway passageway) {
        passageway.setUpdateTime(new Date());
        return passagewayDAO.updateByPrimaryKeySelective(passageway);
    }

    @Override
    public int remove(Long id) {
        return passagewayDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return passagewayDAO.batchRemove(ids);
    }
}
