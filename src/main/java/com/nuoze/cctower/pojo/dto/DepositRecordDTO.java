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
    private BigDecimal amount;
    private String card;
    private String bankAddress;
    private Long parkingId;
}
