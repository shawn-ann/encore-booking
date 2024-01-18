package com.lab.dev.shawn.api.component.entity;

import lombok.Data;

@Data
public class FuiouApiResponse {
    private String mchnt_cd;
    private String message;
    private String resp_code;
    private String resp_desc;

    public FuiouApiResponse() {

    }
}