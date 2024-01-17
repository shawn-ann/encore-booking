package com.lab.dev.shawn.api.util.entity;

import lombok.Data;

@Data
public class FuiouPayApiResponse {
    private String mchnt_cd;
    private String message;
    private String resp_code;
    private String resp_desc;

    public FuiouPayApiResponse() {

    }
}