package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 停车场交易记录
 * @author JiaShun
 */
@Data
public class ParkingTradingRecord implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 停车场ID
     */
    private Long parkingId;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 0:提现 1:收入
     */
    private Integer type;

    /**
     * 交易类型
     */
    private String incomeType;

    /**
     * 交易时间
     */
    private Date payTime;

    private static final long serialVersionUID = 1L;

}