package com.lab.dev.shawn.api.wx.order.dto;

import lombok.Data;

@Data
public class CreateOrderResponseDTO {
    private Long orderId;
    private String orderNumber;
    private int totalFee;
}