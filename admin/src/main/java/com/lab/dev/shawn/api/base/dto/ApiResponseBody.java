package com.lab.dev.shawn.api.base.dto;

import lombok.Getter;

public class ApiResponseBody {
    @Getter
    private int code = 20000;
    @Getter
    private String message = "success";
    @Getter
    private Object data;

    public ApiResponseBody(Object data) {
        this.data = data;
    }

    public ApiResponseBody(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
