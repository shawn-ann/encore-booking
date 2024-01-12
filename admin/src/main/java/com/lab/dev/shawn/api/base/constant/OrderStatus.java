package com.lab.dev.shawn.api.base.constant;

import lombok.Getter;

public enum OrderStatus {
    CREATED,
    PAY_SUCCESS,
    PAY_FAILED,
    REFUND,
    REFUND_SUCCESS,
    REFUND_FAILED;
}
