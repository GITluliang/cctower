package com.nuoze.cctower.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author JiaShun
 * @date 2019-04-30 00:37
 */
@Data
public class DepositRecordDTO {

    private Long accountId;
    /**
     * 提现金额
     */
    private BigDecimal amount;
    /**
     * 卡号
     */
    private String card;
    /**
     *银行地址
     */
    private String bankAddress;
    /**
     * 停车场id
     */
    private Long parkingId;
}
