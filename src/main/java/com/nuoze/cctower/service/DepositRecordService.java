package com.nuoze.cctower.service;


import com.nuoze.cctower.pojo.dto.DepositRecordDTO;
import com.nuoze.cctower.pojo.entity.DepositRecord;
import com.nuoze.cctower.pojo.vo.DepositRecordVO;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @email jiashun@outlook.com
 * @date 2019-04-30 00:00:58
 */
public interface DepositRecordService {

    DepositRecord get(Long id);

    List<DepositRecordVO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(DepositRecordDTO depositRecordDTO);

    int update(DepositRecord depositRecord);

    int remove(Long id);

    int batchRemove(Long[] ids);

    int finishDeposit(Long id);
}
