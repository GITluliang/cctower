package com.nuoze.cctower.pojo.entity;

import java.math.BigDecimal;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * long-long-car
 * @author JiaShun
 */
@Data
public class Car implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 车牌号
     */
    private String number;

    /**
     * 停车场ID
     */
    private Long parkingId;

    /**
     * 0:临时车 1:包月 2:VIP 3:商户车辆
     */
    private Integer parkingType;

    /**
     * 包月车辆开始时间
     */
    private Date monthlyParkingStart;

    /**
     * 包月车辆结束时间
     */
    private Date monthlyParkingEnd;

    /**
     * 状态: 0:禁用 1:正常
     */
    private Integer status;

    /**
     * 内场权限 1：有 0：无
     */
    private Integer infieldPermission;

    /**
     * 创建人
     */
    private Long createId;

    private String openId;

    private Integer freeTime;

    private BigDecimal cost;

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