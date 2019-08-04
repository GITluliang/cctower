package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.dto.PassagewayDTO;
import com.nuoze.cctower.pojo.entity.Passageway;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-17 12:17
 */
public interface PassagewayService {
    int count(Map<String, Object> map);

    List<PassagewayDTO> list(Map<String, Object> map);

    Passageway findById(Long id);

    int save(Passageway passageway);

    int update(Passageway passageway);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
