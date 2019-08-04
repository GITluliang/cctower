package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * parking_record
 * @author JiaShun
 */
@Data
public class ParkingRecord implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 流水号
     */
    private String uuid;

    /**
     * 微信用户唯一标示
     */
    private String openId;

    /**
     * 订单ID(用于微信支付)
     */
    private String orderSn;

    /**
     * prepayId(微信二次支付)
     */
    private String prepayId;

    /**
     * payId(微信支付唯一标识)
     */
    private String payId;

    /**
     * 付款类型：0：线下支付 1：微信支付 2：支付宝支付
     */
    private Integer payType;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 停车场ID
     */
    private Long parkingId;

    /**
     * 入口ID
     */
    private Long entranceId;

    /**
     * 出口ID
     */
    private Long exitId;

    /**
     * 单价(小时)
     */
    private BigDecimal unitPrice;

    /**
     * 停车时长
     */
    private Integer costTime;

    /**
     * 共消费
     */
    private BigDecimal cost;

    /**
     * 支付状态：0：未支付 1：支付成功
     */
    private Integer payStatus;

    /**
     * 是否出场 0：否 1：是
     */
    private Integer status;

    /**
     * 入场时间
     */
    private Date inTime;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 出厂时间
     */
    private Date outTime;

    private static final long serialVersionUID = 1L;

}