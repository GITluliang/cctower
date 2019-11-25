package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.dto.ApiCarLongDTO;
import com.nuoze.cctower.pojo.dto.ApiDTO;
import com.nuoze.cctower.pojo.dto.CarDTO;
import com.nuoze.cctower.pojo.entity.Car;
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
     *
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

    /**
     * 线下：月租车录入
     * @param dto
     * @return
     */
    int saveCarLong(ApiCarLongDTO dto);

    /**
     * 线下：根据UUID进行修改,月租车
     * @param dto
     * @return
     */
    int updateCarLong(ApiCarLongDTO dto);

    /**
     * 线下：根据UUID进行删除月租车
     * @param ids
     * @return
     */
    int batchRemoveCarLong(String[] uuids) ;

    /**
     * 线下：根据UUID进行查询
     * @param uuid
     * @return
     */
    Car findByUuid(String uuid) ;
}
