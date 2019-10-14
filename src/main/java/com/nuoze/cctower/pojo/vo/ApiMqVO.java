package com.nuoze.cctower.pojo.vo;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-04-13 22:35
 */
@Data
public class ApiMqVO {
    /**
     * mq信息类型:
     *  GO_OUT_CAR、RENT_CAR、BILLING_CAR
     */
    private String type;
    private GoOutVO goOutVO;
    private BillingVO billingVO;
    private RentCarVO rentCarVO;
}
