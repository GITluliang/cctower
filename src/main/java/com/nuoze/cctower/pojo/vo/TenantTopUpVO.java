package com.nuoze.cctower.pojo.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-05-04 19:27
 */
@Data
public class TenantTopUpVO {

    private Long userId;
    private String parkingName;
    private String businessName;
    private BigDecimal balance;
}
