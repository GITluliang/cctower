package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信账单记录
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class TopUpRecord implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户open_id
     */
    private String openId;
    /**
     * 订单id
     */
    private String orderSn;

    private String prepayId;
    /**
     * 微信订单号
     */
    private String payId;
    /**
     * 停车场id
     */
    private Long parkingId;

    /**
     * 1:充值 0：消费
     */
    private Integer billingType;

    /**
     * 消费金额
     */
    private BigDecimal amount;

    /**
     * 消费后账户余额
     */
    private BigDecimal balance;

    /**
     * 支付状态：0：未支付 1：支付成功
     */
    private Integer payStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}