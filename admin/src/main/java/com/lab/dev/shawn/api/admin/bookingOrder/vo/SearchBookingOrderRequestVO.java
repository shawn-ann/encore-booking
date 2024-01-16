package com.lab.dev.shawn.api.admin.bookingOrder.vo;

import lombok.Data;

@Data
public class SearchBookingOrderRequestVO {
    private int page;
    private int limit;
    private String concertName;
    private String agentMobile;
    private String[] dateRange;
}
