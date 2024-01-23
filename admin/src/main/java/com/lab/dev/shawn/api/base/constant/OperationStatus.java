package com.lab.dev.shawn.api.base.constant;

import lombok.Getter;

public enum OperationStatus {
    CANCEL_ORDER("取消订单"),
    CREATE_ORDER("创建订单"),
    PAY_SUCCESS("支付成功"),
    PAY_FAILED("支付失败"),
    APPLY_REFUND("申请退款"),
    REFUND_SUCCESS("退款成功"),
    REFUND_FAILED("退款失败");
    @Getter
    private String name;

    OperationStatus(String name) {
        this.name = name;
    }
}
