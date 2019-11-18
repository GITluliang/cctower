package com.nuoze.cctower.pojo.dto;

import com.nuoze.cctower.pojo.entity.BillingDetail;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-07-03 23:26
 */
@Data
@Accessors(chain = true)
public class BillingDetailDTO extends BillingDetail {

    private String parkingName;
}
