package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.Account;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author JiaShun
 * @date 2019-04-28 01:09
 */
@Data
public class AccountVO extends Account {

    private String parkingName;
    /**
     * 可提现金额
     */
    private BigDecimal withdrawalAmount;
}
