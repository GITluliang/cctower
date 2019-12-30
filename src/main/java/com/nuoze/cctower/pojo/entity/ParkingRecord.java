package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * parking_record   停车记录
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
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
     * 微信用户id
     */
    private String openId;

    /**
     * 订单ID
     *  最终支付订单号，如果是小程序支付保存小程序订单号，付款码保存付款码订单号
     */
    private String orderSn;

    /**
     * 预付款id
     */
    private String prepayId;

    /**
     * 支付id
     */
    private String payId;

    /**
     * 付款类型：0：线下 1：微信 2：支付宝 3:vip
     *      *      4：月租车、5：商户车辆、6：无需支付
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
     * 服务费
     */
    private BigDecimal serviceCharge;

    /**
     * 停车时长
     */
    private Integer costTime;

    /**
     * 车费
     */
    private BigDecimal cost;

    /**
     * 支付状态：0：未支付 1：支付成功
     */
    private Integer payStatus;

    /**
     * 是否出场 0：否 1：是 2：待出场
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

    /**
     * 小程序支付订单号（后加字段）
     */
    private String appletOrderSn ;

    /**
     * 付款码支付订单（后加字段）
     */
    private String qrCodeOrderSn ;

    /**
     * 支付宝支付订单（后加字段）
     */
    private String alPayOrderSn ;
    /**
     * 主键
     */
    private Long advanceId;

    private static final long serialVersionUID = 1L;

}