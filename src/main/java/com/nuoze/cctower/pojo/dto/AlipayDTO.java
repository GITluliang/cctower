package com.nuoze.cctower.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author luliang
 * @Date 2019-12-24 15:13
 */
@Data
@Accessors(chain = true)
public class AlipayDTO {
    /**
     * 停车记录主键id
     */
    private Long recordId;
    /**
     * 要支付的钱
     */
    private String money;
}
