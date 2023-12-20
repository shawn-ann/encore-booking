package com.lab.dev.shawn.admin.base.constant;

public enum BaseStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");
    private String code;

    BaseStatus(String code) {
        this.code = code;
    }
}
