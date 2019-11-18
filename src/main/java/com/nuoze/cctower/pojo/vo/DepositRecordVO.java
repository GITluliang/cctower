package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.DepositRecord;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author JiaShun
 * @date 2019-04-30 01:29
 */
@Data
@Accessors(chain = true)
public class DepositRecordVO extends DepositRecord {
    private String userName;
    private String parkingName;
    private String createTimeStr;
    private String transferTimeStr;
}
