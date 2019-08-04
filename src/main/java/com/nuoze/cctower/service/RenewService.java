package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.vo.RenewRecordVO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-05-15 00:37
 */
public interface RenewService {

    Pair<Integer, List<RenewRecordVO>> list(Map<String, Object> map);
}
