package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-04-23 00:50
 */
@Data
@Accessors(chain = true)
public class TopUpRecordVO {

    private String type;
    private String time;
    private String amount;
}
