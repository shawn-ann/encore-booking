package com.lab.dev.shawn.api.base.exception;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import lombok.Getter;

public class BaseException extends Exception {
    @Getter
    private int code;
    @Getter
    private String message;

    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(BaseExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }
}
