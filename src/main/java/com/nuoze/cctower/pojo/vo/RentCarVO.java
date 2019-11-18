package com.nuoze.cctower.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author JiaShun
 * @date 2019-04-26 00:44
 */
@Data
@Accessors(chain = true)
public class RentCarVO {

    private String type;
    private RentCarApiEntity rentCarApiEntity;
}
