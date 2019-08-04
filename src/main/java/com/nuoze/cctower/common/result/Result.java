package com.nuoze.cctower.common.result;

import lombok.Data;

/**
 * @author JiaShun
 * @date 2019-01-15 00:28
 */
@Data
public class Result {

    private Integer code;

    private String msg;

    private Object data;
}
