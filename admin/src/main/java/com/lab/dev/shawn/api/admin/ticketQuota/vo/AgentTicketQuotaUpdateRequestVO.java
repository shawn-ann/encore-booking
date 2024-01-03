package com.lab.dev.shawn.api.admin.ticketQuota.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentTicketQuotaUpdateRequestVO {
    private Long id;
    private int price;

    public int getPrice() {
        return price * 100;
    }
}
