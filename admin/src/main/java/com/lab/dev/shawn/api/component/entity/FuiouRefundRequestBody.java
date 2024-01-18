package com.lab.dev.shawn.api.component.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class FuiouRefundRequestBody {
    private String refund_order_id;
    private String refund_order_date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    private String pay_order_date;
    private String pay_order_id;
    private String refund_amt;
}
