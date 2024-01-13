package com.lab.dev.shawn.api.base.constant;

import lombok.Getter;

public enum OperationStatus {
    CREATE("创建订单"),
    PAY(""),
    PAY_SUCCESS(""),
    REFUNDING(""),
    REFUND_SUCCESS(""),
    REFUND_FAILED("");
    @Getter
    private String name;

    OperationStatus(String name) {
        this.name = name;
    }
}
