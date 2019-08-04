package com.nuoze.cctower.pojo.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-05-01 17:17
 */
@Data
public class FinanceVO {

    private String parkingName;
    private String carNumber;
    private String entranceName;
    private String exitName;
    private String inTime;
    private String outTime;
    private Integer payType;
    private BigDecimal cost;
}
