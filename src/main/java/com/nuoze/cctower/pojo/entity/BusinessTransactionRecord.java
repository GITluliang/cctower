package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户交易记录
 * business_transaction_record
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class BusinessTransactionRecord implements Serializable {
    private Long id;
    /**
     * 商户id
     */
    private Long userId;
    /**
     * 0: 支出 1：物业充值 2. 退款 3.微信充值
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
    /**
     * 车辆免费时长
     */
    private Integer freeTime;
    /**
     * 商户状态: 0:时长商户 1:时长劵商户
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

}