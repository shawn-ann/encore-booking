package com.lab.dev.shawn.api.component.entity;

import lombok.Data;

@Data
public class WxOpenIdApiResponse {
    private int errcode;
    private String errmsg;
    private String openid;

    public WxOpenIdApiResponse() {

    }
}