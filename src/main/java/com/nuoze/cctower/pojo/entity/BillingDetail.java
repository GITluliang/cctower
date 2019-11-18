package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * billing_detail   收费规则详情表
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class BillingDetail implements Serializable {
    private Long id;

    private Long parkingId;

    /**
     * 1:分时计费 2：分段计费
     */
    private Integer type;

    /**
     * 0：分钟 1：小时
     */
    private Integer unitType;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    private BigDecimal one;

    private BigDecimal two;

    private BigDecimal three;

    private BigDecimal four;

    private BigDecimal five;

    private BigDecimal six;

    private BigDecimal seven;

    private BigDecimal eight;

    private BigDecimal nine;

    private BigDecimal ten;

    private BigDecimal eleven;

    private BigDecimal twelve;

    private BigDecimal thirteen;

    private BigDecimal fourteen;

    private BigDecimal fifteen;

    private BigDecimal sixteen;

    private BigDecimal seventeen;

    private BigDecimal eighteen;

    private BigDecimal nineteen;

    private BigDecimal twenty;

    private BigDecimal twentyOne;

    private BigDecimal twentyTwo;

    private BigDecimal twentyThree;

    private BigDecimal twentyFour;

    private Date createTime;

    private Date updateTime;

    /**
     * 所属物业公司ID(新加字段)
     */
    private Long userId ;

    private static final long serialVersionUID = 1L;
}