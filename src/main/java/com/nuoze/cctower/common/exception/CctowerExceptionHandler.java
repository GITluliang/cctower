package com.nuoze.cctower.common.exception;

import com.nuoze.cctower.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author JiaShun
 * @date 2019-05-19 00:35
 */
@Slf4j
@ControllerAdvice(basePackages = "com.nuoze.cctower.rest")
public class CctowerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R handlerUnexpectedException(Exception e) {
        log.error("[EXCEPTION HANDLER] cctower service unexpected exception", e);
        return R.error();
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public R handlerUnexpectedValidateException(Exception e) {
        log.warn("[EXCEPTION HANDLER] cctower service binding exception", e);
        return R.error(401,  "参数错误");
    }
}
