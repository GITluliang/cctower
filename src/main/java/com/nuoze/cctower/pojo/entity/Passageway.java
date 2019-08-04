package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * passageway
 * @author JiaShun
 */
@Data
public class Passageway implements Serializable {
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 出入口摄像头IP
     */
    private String ip;

    /**
     * 停车场ID
     */
    private Long parkingId;

    /**
     * 0：入口，1：出口
     */
    private Integer type;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}