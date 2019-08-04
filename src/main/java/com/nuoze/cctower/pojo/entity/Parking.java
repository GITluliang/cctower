package com.nuoze.cctower.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * parking
 * @author JiaShun
 */
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
     * 负责人联系方式
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

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}