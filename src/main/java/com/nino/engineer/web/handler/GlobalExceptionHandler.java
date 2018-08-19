package com.nino.engineer.web.handler;

import com.nino.engineer.domain.Result;
import com.nino.engineer.exception.NoPermissionException;
import com.nino.engineer.web.constants.Constants;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Created by ss on 18/8/11.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = Throwable.class)
    public Result handleThrowable(Throwable throwable) {
        throwable.printStackTrace();
        return Result.fail(Constants.RSPCODE_FAILED, "请求失败");
    }

    @ExceptionHandler(value = NoPermissionException.class)
    public Result handlePermission(NoPermissionException e) {
        e.printStackTrace();
        return Result.fail(Constants.RSPCODE_STAFF_NOAUTHORIZATION, "没有权限");
    }
}
