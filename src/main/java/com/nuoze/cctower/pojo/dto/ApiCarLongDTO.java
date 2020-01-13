package com.nuoze.cctower.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author luliang
 * @Date 2019-11-25 10:43
 */
@Data
@Accessors( chain = true)
public class ApiCarLongDTO {
    /**
     * 线下车牌唯一标识
     */
    private String uuid;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 停车场id
     */
    private Long parkingId;
    /**
     * 开始日期
     */
    private String beginDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 车主姓名（新增）
     */
    private String name;

    /**
     * 联系方式（新增）
     */
    private String phone;

    /**
     * 公司名称（新增）
     */
    private String corporateName;

    /**
     * 替换车牌1
     */
    private String numberOne;

    /**
     * 替换车牌2
     */
    private String numberTow;
}
