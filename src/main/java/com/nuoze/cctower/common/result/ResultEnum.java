package com.nuoze.cctower.common.result;

/**
 * @author JiaShun
 * @date 2019-01-18 21:37
 */
public enum ResultEnum {

    /**
     * 没有权限
     */
    PERMISSION_DENIED(401, "PERMISSION_DENIED"),

    /**
     * 请求失败，
     */
    SERVER_ERROR(400, "SERVER_ERROR"),

    /**
     * 参数错误
     */
    INVALID_PARAM(40301, "PARAMETER_ERROR"),

    /**
     * 微信错误
     */
    WX_SERVER_ERROR(402, "WX_SERVER_ERROR");

    private int code;
    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
