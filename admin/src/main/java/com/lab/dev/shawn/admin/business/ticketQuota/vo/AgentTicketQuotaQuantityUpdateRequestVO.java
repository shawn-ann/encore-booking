package com.lab.dev.shawn.admin.business.ticketQuota.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentTicketQuotaQuantityUpdateRequestVO {
    private Long id;
    private int quantity;
    private String operation;
}
