package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.RenewRecord;
import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-05-15 00:58
 */
@Data
public class RenewRecordVO extends RenewRecord {

    private String parkingName;
    private String createDate;
}
