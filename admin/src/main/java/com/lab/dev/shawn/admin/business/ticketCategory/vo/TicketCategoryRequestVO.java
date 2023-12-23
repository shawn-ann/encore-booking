package com.lab.dev.shawn.admin.business.ticketCategory.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketCategoryRequestVO {
    private Long id;
    private Long concertId;
    private String name;
}
