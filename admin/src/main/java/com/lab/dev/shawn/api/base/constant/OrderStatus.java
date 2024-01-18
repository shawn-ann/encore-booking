package com.lab.dev.shawn.api.base.constant;

import lombok.Getter;

public enum OrderStatus {
    CANCEL("已取消"),
    CREATED("待支付"),
    PAID("已支付"),
    REFUNDING("退款中"),
    REFUNDED("已退款");
    @Getter
    private String name;

    OrderStatus(String name) {
        this.name = name;
    }
}
