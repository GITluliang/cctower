package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * renew_record:小程序续费记录
 *
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class RenewRecord implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 小程序用open_id
     */
    private String openId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 准备支付ID
     */
    private String prepayId;

    /**
     * 支付ID
     */
    private String payId;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 停车场ID
     */
    private Long parkingId;

    /**
     * 续费月数
     */
    private Integer monthCount;

    /**
     * 续费金额
     */
    private BigDecimal cost;

    /**
     * 服务费
     */
    private BigDecimal serviceCharge;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}