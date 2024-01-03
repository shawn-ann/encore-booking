package com.lab.dev.shawn.api.admin.ticketQuota.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentTicketQuotaRequestVO {
    private Long id;
    private Long agentId;
    private Long concertId;
    private Long inventoryId;
    private int quantity;
    private int price;

    public int getPrice() {
        return price * 100;
    }
}
