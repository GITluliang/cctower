package com.nuoze.cctower.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-04-10 00:22
 */
@Data
@Accessors(chain = true)
public class WalletDTO {

    private String openId;
    private String amount;
}
