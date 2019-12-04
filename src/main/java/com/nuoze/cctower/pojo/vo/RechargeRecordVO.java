package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.RechargeRecord;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author luliang
 * @Date 2019-12-04 18:12
 */
@Data
@Accessors(chain = true)
public class RechargeRecordVO extends RechargeRecord {
    private String createTimeStr ;
}
