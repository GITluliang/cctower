package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.entity.BusinessTransactionRecord;
import com.nuoze.cctower.pojo.vo.BusinessTransactionRecordVO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-05-05 01:29
 */
public interface BusinessTransactionRecordService {
    Pair<Integer, List<BusinessTransactionRecordVO>> list(Map<String, Object> map);
}
