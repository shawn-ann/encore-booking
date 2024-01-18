package com.lab.dev.shawn.api.component.entity;

import lombok.Data;

@Data
public class FuiouRefundResponseBody {
    private String mchnt_cd;
    private String refund_order_date;
    private String refund_order_id;
    private String pay_order_date;
    private String pay_order_id;
    private String refund_amt;
    private String refund_st;
    private String refund_fas_date;
    private String refund_fas_ssn;
}
