package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.dto.ApiDTO;
import com.nuoze.cctower.pojo.entity.Passageway;
import com.nuoze.cctower.pojo.vo.ApiCheckCarVO;
import com.nuoze.cctower.pojo.vo.ApiOutVO;
import com.nuoze.cctower.pojo.vo.ApiPayStatusVO;

/**
 * @author JiaShun
 * @date 2019-03-19 23:20
 */
public interface ApiService {

    boolean in(ApiDTO apiDTO);

    /**
     * 出口调用
     * @param apiDTO
     * @return
     */
    ApiOutVO out(ApiDTO apiDTO);

    boolean paid(ApiDTO apiDTO);

    ApiCheckCarVO isRentFreeCar(ApiDTO apiDTO);

    int addPassageway(Passageway passageway);

    void deletePassageway(Long parkingId, String ip);

    boolean changeCarNumber(ApiDTO apiDTO);

    boolean initData(ApiDTO apiDTO, String rentCarType);

    ApiPayStatusVO checkPayStatus(String uuid);
}
