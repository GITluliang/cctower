package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.vo.ParkingTradingRecordVO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-05-16 00:00
 */
public interface ParkingTradingRecordService {
    Pair<Integer, List<ParkingTradingRecordVO>> list(Map<String, Object> params);
}
