package com.lab.dev.shawn.api.admin.ticketQuota.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AgentTicketQuotaRequestVO {
    private Long id;
    private Long agentId;
    private Long concertId;
    private Long inventoryId;
    private int quantity;
    private BigDecimal price;

    public int getPrice() {
        return price.multiply(new BigDecimal(100)).intValue();
    }
}
