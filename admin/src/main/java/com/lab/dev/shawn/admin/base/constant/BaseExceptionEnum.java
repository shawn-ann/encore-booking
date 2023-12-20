package com.lab.dev.shawn.admin.base.constant;

import lombok.Getter;

public enum BaseExceptionEnum {
    INCORRECT_USER_CREDENTIAL(5000, "用户名或密码错误！"),
    UNAUTHORIZED(5001, "未登录！"),
    ;
    @Getter
    private int code;
    @Getter
    private String message;

    BaseExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}