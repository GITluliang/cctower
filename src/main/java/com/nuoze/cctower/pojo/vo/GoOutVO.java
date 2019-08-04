package com.nuoze.cctower.pojo.vo;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-04-27 00:45
 */
@Data
public class GoOutVO {
    private Long parkingId;
    private String ip;
    private String carNumber;
    /**
     * 0：临时车辆 1：月租车 2：停车场VIP 3：商户VIP
     */
    private Integer type;
}
