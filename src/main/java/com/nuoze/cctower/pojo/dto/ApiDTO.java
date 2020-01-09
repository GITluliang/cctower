package com.nuoze.cctower.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * @author JiaShun
 * @date 2019-03-19 23:11
 */
@Data
@Accessors(chain = true)
public class ApiDTO {
    /**
     * 停车场id
     */
    private Long parkingId;
    /**
     * IP
     */
    private String ip;
    /**
     *新车牌号
     */
    private String carNumber;
    /**
     * 老车牌号
     */
    private String oldCarNumber;
    /**
     * 入场时间
     */
    private String inTime;

    public boolean freeCarCheck() {
        return parkingId != null && StringUtils.isNotEmpty(carNumber);
    }
    public boolean check() {
        return parkingId != null && StringUtils.isNotEmpty(ip) && StringUtils.isNotEmpty(carNumber);
    }
    public boolean checkCarNumber() {
        return parkingId != null && StringUtils.isNotBlank(oldCarNumber) && StringUtils.isNotBlank(carNumber);
    }

    public boolean checkInit() {
        return parkingId != null;
    }
}
