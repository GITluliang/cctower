package com.nuoze.cctower.pojo.vo;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-05-01 17:17
 */
@Data
@Accessors(chain = true)
public class FinanceVO {

    private String parkingName;
    private String carNumber;
    private String entranceName;
    private String exitName;
    private String inTime;
    private String outTime;
    private String payTime;
    private Integer payType;
    private BigDecimal cost;
    /**
     * 停车时长
     */
    private String costTime;
    /**
     * 是否出场 0：否 1：是 2：待出场
     */
    private Integer status;
    /**
     * 支付状态：0：未支付 1：支付成功
     */
    private Integer payStatus;
}
