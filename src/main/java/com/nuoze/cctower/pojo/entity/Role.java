package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * role
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class Role implements Serializable {
    private Long id;

    private String name;

    private String sign;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}