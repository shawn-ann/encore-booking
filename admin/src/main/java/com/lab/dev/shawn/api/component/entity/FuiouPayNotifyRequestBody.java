package com.lab.dev.shawn.api.component.entity;

import lombok.Data;

@Data
public class FuiouPayNotifyRequestBody {
    private String mchnt_cd;
    private String order_date;
    private String order_id;
    private String fy_order_id;
    private int order_amt;
    private String order_st;
    private String order_pay_type;
    private String order_fas_date;
    private String order_fas_ssn;
    private String pay_ssn;
}
