package com.lab.dev.shawn.admin.business.inventory.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryRequestVO {
    private Long id;
    private Long concertId;
    private Long ticketCategoryId;
    private Long sessionId;
    private int quantity;
}
