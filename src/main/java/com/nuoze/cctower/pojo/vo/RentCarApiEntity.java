package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-04-26 00:46
 */
@Data
@Accessors(chain = true)
public class RentCarApiEntity {

    private Long cloudId;
    private String number;
    private String rentCarStart;
    private String rentCarEnd;
    private Integer status;
    private Integer infieldPermission;
    private String uuid;
}
