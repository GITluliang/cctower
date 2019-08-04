package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.BillingDetail;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author JiaShun
 * @date 2019-04-26 00:50
 */
@Data
public class BillingApiEntity {
    private Long cloudId;
    private Integer freeTime;
    private Integer wechatDiscount;
    private Integer alipayDiscount;
    private BigDecimal dayCost;
    private BillingDetail detail;
}
