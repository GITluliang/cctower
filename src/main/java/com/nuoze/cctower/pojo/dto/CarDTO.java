package com.nuoze.cctower.pojo.dto;

import com.nuoze.cctower.pojo.entity.Car;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-03-16 00:46
 */
@Data
public class CarDTO extends Car {

    private String beginDate;
    private String endDate;
    private String parkingName;
}
