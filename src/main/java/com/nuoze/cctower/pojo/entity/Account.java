package com.nuoze.cctower.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * account
 * @author JiaShun
 */
@Data
public class Account implements Serializable {
    private Long id;

    private BigDecimal balance;

    private Integer serviceCharge;

    private String card;

    private String bank;

    private String bankAddress;

    private Long parkingId;

    private Date createTime;

    private static final long serialVersionUID = 1L;

}