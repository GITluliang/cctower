package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-04-27 00:45
 */
@Data
@Accessors(chain = true)
public class GoOutVO {
    /**
     * 停车场id
     */
    private Long parkingId;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 0：临时车辆 1：月租车 2：停车场VIP 3：商户VIP
     */
    private Integer type;
}
