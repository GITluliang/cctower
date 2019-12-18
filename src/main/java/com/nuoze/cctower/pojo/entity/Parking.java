package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * parking
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class Parking implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 停车场名称
     */
    private String name;

    /**
     * 所属物业公司ID
     */
    private Long userId;

    /**
     * 位置
     */
    private String location;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 联系人（新增）
     */
    private String contacts;
    /**
     * 联系邮箱（新增）
     */
    private String email;
    /**
     * 月租车重复出入场：0:禁用 1：启用
     */
    private Integer rentStatic;
    /**
     * VIP车重复出入场：0:禁用 1：启用
     */
    private Integer vipStatic;
    /**
     * 手机开闸：0:禁用 1：启用
     */
    private Integer passagewayStatic;

    private static final long serialVersionUID = 1L;
}