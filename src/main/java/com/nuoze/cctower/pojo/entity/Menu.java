package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * menu
 * @author JiaShun
 */
@Data
public class Menu implements Serializable {
    private Long id;

    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}