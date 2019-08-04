package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.DepositRecord;
import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-04-30 01:29
 */
@Data
public class DepositRecordVO extends DepositRecord {
    private String userName;
    private String parkingName;
}
