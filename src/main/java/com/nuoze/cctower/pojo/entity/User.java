package com.nuoze.cctower.pojo.entity;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 *
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {
    private Long id;

    private BigDecimal balance;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 名字
     */
    private String name;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 地址
     */
    private String address;

    /**
     * 负责人名字
     */
    private String principal;

    /**
     * 负责人联系方式
     */
    private String phone;

    /**
     * 用户类型：0：物业公司 1：商户
     */
    private Integer type;

    /**
     * 当用户为商户，此为对应的停车场ID
     */
    private Long parkingId;

    /**
     * 状态: 0:禁用 1:正常
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 商户剩余时长劵
     */
    private Integer timeCoupon;

    private static final long serialVersionUID = 1L;

}