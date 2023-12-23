package com.lab.dev.shawn.admin.base.constant;

import lombok.Getter;

public enum BaseExceptionEnum {
    INCORRECT_USER_CREDENTIAL(5000, "用户名或密码错误！"),
    UNAUTHORIZED(5001, "未登录！"),
    NOT_FOUND_MATCH_RECORD(5002, "未找到匹配记录！"),
    NOT_ALLOWED_OPERATION(5003, "操作不允许！"),
    UPDATE_FAILED(5004, "更新失败，请稍后重试！"),
    CREATE_FAILED(5005, "创建失败，请稍后重试！"),
    DUPLICATED_RECORD(5006, "已存在该记录！"),
    UNALLOCATED_QUANTITY_NOT_ENOUGTH(5008, "未分配数量不足！"),
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
