package com.nuoze.cctower.pojo.entity;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * long-long-car
 *
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
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
     * 0:临时车 1:包月 2:永久月租 3:商户车辆 4:特殊车辆 5:单次VIP 6:时长劵商户车辆
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
    /**
     * 小程序openId
     */
    private String openId;
    /**
     * 车辆免费时长
     */
    private Integer freeTime;
    /**
     * 费用
     */
    private BigDecimal cost;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 车主姓名（新增）
     */
    private String name;

    /**
     * 联系方式（新增）
     */
    private String phone;

    /**
     * 公司名称（新增）
     */
    private String corporateName;

    /**
     * 商户剩余时长劵
     */
    private Integer timeCoupon;

    /**
     * 线下车牌唯一标识
     */
    private String uuid;

    /**
     * 替换车牌1
     */
    private String numberOne;

    /**
     * 替换车牌2
     */
    private String numberTow;
    private static final long serialVersionUID = 1L;

}