package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * billing
 * @author JiaShun
 */
@Data
public class Billing implements Serializable {

    private Long id;

    private Long parkingId;

    /**
     * 月租费用
     */
    private BigDecimal monthlyPrice;

    /**
     * 免费停车时间(分钟)
     */
    private Integer freeTime;

    /**
     * 缴费后预留出场时间
     */
    private Integer paidFreeTime;

    /**
     * 微信折扣百分比
     */
    private Integer wechatDiscount;

    /**
     * 支付宝折扣百分比
     */
    private Integer alipayDiscount;

    /**
     * 24小时最高收费
     */
    private BigDecimal dayCost;

    /**
     * 所属物业公司ID(新加字段)
     */
    private Long userId ;
    private static final long serialVersionUID = 1L;

}