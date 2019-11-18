package com.nuoze.cctower.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * account
 * @author JiaShun
 */
@Data
@Accessors(chain = true)
public class Account implements Serializable {
    private Long id;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 服务费，单位千分比
     */
    private Integer serviceCharge;
    /**
     * 银行卡号
     */
    private String card;
    /**
     * 银行
     */
    private String bank;
    /**
     * 开户行
     */
    private String bankAddress;
    /**
     * 停车场id
     */
    private Long parkingId;
    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

}