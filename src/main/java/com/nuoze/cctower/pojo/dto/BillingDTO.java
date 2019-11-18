package com.nuoze.cctower.pojo.dto;

import com.nuoze.cctower.pojo.entity.Billing;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-04-05 15:50
 */
@Data
@Accessors(chain = true)
public class BillingDTO extends Billing {

    private String parkingName;
}
