package com.nuoze.cctower.common.result;

/**
 * @author JiaShun
 * @date 2019-01-15 00:37
 */
public class ResponseResult {

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("SUCCESS");
        result.setData(data);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result fail(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result fail(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMessage());
        return result;
    }

}
