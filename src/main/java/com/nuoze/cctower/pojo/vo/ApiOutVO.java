package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author JiaShun
 * @date 2019-03-20 00:03
 */
@Data
@Accessors(chain = true)
public class ApiOutVO {
    /**
     * 支付状态：0：未支付 1：支付成功
     */
    private Integer paid;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 0：临时车辆 1：月租车 2：停车场VIP 3：商户VIP
     */
    private Integer type;
    /**
     * 费用
     */
    private String cost;
    /**
     * 付款码url
     */
    private String codeUrl;
    /**
     * 全局唯一标识符
     */
    private String uuid;
    /**
     * 服务费
     */
    private String serviceCharge;
    /**
     * 当前记录对应的提前支付信息（车主提起付款，如果出厂时间>预留时间，重新开始计费）
     */
    private ApiOutVO advance;
    /**
     * 付款时间
     */
    private String payTime;
}
