package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * business_transaction_record
 * @author JiaShun
 */
@Data
public class BusinessTransactionRecord implements Serializable {
    private Long id;

    private Long userId;

    /**
     * 0: 支出 1：充值
     */
    private Integer type;

    private String carNumber;

    /**
     * 交易金额
     */
    private BigDecimal amount;
    private BigDecimal balance;

    private Date createTime;

    private static final long serialVersionUID = 1L;

}