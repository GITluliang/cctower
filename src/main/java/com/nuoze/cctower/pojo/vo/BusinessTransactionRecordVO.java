package com.nuoze.cctower.pojo.vo;

import com.nuoze.cctower.pojo.entity.BusinessTransactionRecord;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户交易记录
 * business_transaction_record
 * @author JiaShun
 */
@Data
public class BusinessTransactionRecordVO extends BusinessTransactionRecord {

    private String createTimeStr;
    private String businessName;

}