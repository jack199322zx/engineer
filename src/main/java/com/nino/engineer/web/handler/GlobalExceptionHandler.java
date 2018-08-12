package com.nino.engineer.web.handler;

import com.nino.engineer.domain.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Created by ss on 18/8/11.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final String RSPCODE_FAILED = "1";

    @ExceptionHandler(value = Throwable.class)
    public Result handleThrowable(Throwable throwable) {
        throwable.printStackTrace();
        return Result.fail(RSPCODE_FAILED, "请求失败");
    }
}
