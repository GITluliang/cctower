package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * order_number 订单编号
 * @Author luliang
 * @Date 2019-10-21 15:57
 */
@Data
public class OrderNumber implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 停车记录ID
     */
    private Long parkingRecordId;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}