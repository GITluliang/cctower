package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.RenewRecord;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-05-15 00:58
 */
@Data
@Accessors(chain = true)
public class RenewRecordVO extends RenewRecord {

    private String parkingName;
    private String createDate;
}
