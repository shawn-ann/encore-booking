package com.lab.dev.shawn.api.admin.ticketQuota.vo;

import com.lab.dev.shawn.api.base.constant.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
public class AgentTicketQuotaResponseVO {
    private Long id;
    private String concertName;
    private String sessionName;
    private String ticketCategoryName;
    private String agentName;
    private int totalQuantity;
    private int remainingQuantity;
    private int price;
    private BaseStatus status;

    public String getPrice() {
        return new BigDecimal(price).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString();
    }
}
