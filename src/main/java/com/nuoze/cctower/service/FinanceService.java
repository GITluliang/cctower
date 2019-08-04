package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.vo.FinanceVO;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author JiaShun
 * @date 2019-05-01 17:23
 */
public interface FinanceService {

    Pair<Integer, List<FinanceVO>> list(Map<String, Object> map);

}
