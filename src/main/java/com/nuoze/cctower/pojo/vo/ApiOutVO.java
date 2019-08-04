package com.nuoze.cctower.pojo.vo;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-03-20 00:03
 */
@Data
public class ApiOutVO {

    private Integer paid;
    private String carNumber;
    /**
     * 0：临时车辆 1：月租车 2：停车场VIP 3：商户VIP
     */
    private Integer type;
    private String cost;
    private String codeUrl;
    private String uuid;
}
