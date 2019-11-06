package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户交易记录
 * business_transaction_record
 * @author JiaShun
 */
@Data
public class BusinessTransactionRecord implements Serializable {
    private Long id;
    /**
     * 商户id
     */
    private Long userId;
    /**
     * 0: 支出 1：充值 2. 退款
     */
    private Integer type;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

}