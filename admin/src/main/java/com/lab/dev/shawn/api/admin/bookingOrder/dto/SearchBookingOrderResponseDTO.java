package com.lab.dev.shawn.api.admin.bookingOrder.dto;

import com.lab.dev.shawn.api.base.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class SearchBookingOrderResponseDTO {
    private Long id;
    private String orderNumber;
    private String concertName;
    private String sessionName;

    private String ticketCategoryName;
    private OrderStatus status;
    private LocalDateTime createDate;
    private int buyCount;
    private int totalFee;

    public String getTotalFee() {
        return new BigDecimal(totalFee).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public String getStatusName() {
        return status.getName();
    }
}
