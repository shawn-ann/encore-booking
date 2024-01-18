package com.lab.dev.shawn.api.component.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class FuiouPayRequestBody {
    private String order_id;
    private String order_date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    private int order_amt;
    private String goods_name;
    private String goods_detail;
    private String openid;
}
