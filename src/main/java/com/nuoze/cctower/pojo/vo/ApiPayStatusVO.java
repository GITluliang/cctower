package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-07-31 00:39
 */
@Data
@Accessors(chain = true)
public class ApiPayStatusVO {
    String uuid;
    Integer payStatus;
}
