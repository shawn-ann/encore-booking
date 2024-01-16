package com.lab.dev.shawn.api.admin.ticketQuota.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AgentTicketQuotaUpdateRequestVO {
    private Long id;
    private BigDecimal price;

    public int getPrice() {
        return price.multiply(new BigDecimal(100)).intValue();
    }
}
