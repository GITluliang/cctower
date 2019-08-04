package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * deposit_record
 * @author JiaShun
 */
@Data
public class DepositRecord implements Serializable {
    private Long id;

    /**
     * 提现金额
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal serviceCharge;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 停车场ID
     */
    private Long parkingId;

    /**
     * 卡号
     */
    private String card;

    /**
     * 开户行
     */
    private String bankAddress;

    /**
     * 0：未转账 1：转账成功
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 转账完成时间
     */
    private Date transferTime;

    private static final long serialVersionUID = 1L;

}