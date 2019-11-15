package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * role
 * @author JiaShun
 */
@Data
public class Role implements Serializable {
    private Long id;

    private String name;

    private String sign;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}