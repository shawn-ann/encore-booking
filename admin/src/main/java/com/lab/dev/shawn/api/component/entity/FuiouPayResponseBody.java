package com.lab.dev.shawn.api.component.entity;

import lombok.Data;

@Data
public class FuiouPayResponseBody {
    private String mchnt_cd;
    private String order_date;
    private String order_id;
    private String order_amt;
    private String order_pay_type;
    private String order_info;
}
