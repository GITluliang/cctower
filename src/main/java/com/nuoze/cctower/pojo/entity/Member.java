package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * member
 * @author JiaShun
 */
@Data
public class Member implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * openId
     */
    private String openId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户名
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

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