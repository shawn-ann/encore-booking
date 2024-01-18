package com.lab.dev.shawn.api.base.constant;

import lombok.Getter;

public enum BaseExceptionEnum {
    INCORRECT_USER_CREDENTIAL(50000, "用户名或密码错误！"),
    SYSTEM_ERROR(50000, "系统错误！"),
    NOT_LOGIN(50001, "未登录！"),
    NOT_FOUND_MATCH_RECORD(50002, "未找到匹配记录！"),
    NOT_ALLOWED_OPERATION(5003, "操作不允许！"),
    UPDATE_FAILED(50004, "更新失败，请稍后重试！"),
    CREATE_FAILED(50005, "创建失败，请稍后重试！"),
    DUPLICATED_RECORD(50006, "已存在该记录！"),
    UNALLOCATED_QUANTITY_NOT_ENOUGTH(50008, "未分配数量不足！"),
    MOBILE_ALREADY_EXIST(50009, "手机号已存在！"),
    INSUFFICIENT_STOCK(50010, "库存不足，请刷新重试！"),
    OPERATION_FAILED(50011, "操作失败，请稍后重试！"),


    SMS_CODE_INCORRECT(40000, "验证码错误！"),
    SMS_CODE_ALREADY_SEND(40000, "您在一分钟内已经发送过验证码了，请稍后重试！"),

    USER_NOT_EXIST(40000, "用户不存在！"),

    CURRENT_USER_CAN_NOT_CREAT_ORDER(50011, "当前用户不允许下单，请联系管理员！"),

    RETRIEVE_OPENID_FAILED(5003, "获取OPENID失败！"),
    CALL_THIRD_PARTY_API_ERROR(5003, "调用第三方接口异常！"),
    ORDER_STATUS_CANNOT_PAY(5003, "当前订单状态不可以支付！"),
    ORDER_NOT_EXIST(5003, "订单不存在！"),
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
