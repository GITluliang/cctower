package com.nuoze.cctower.pojo.dto;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author JiaShun
 * @date 2019-03-19 23:11
 */
@Data
public class ApiDTO {

    private Long parkingId;
    private String ip;
    private String carNumber;
    private String oldCarNumber;

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
