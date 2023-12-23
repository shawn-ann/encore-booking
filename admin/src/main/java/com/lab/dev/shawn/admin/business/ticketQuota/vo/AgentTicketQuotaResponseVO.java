package com.lab.dev.shawn.admin.business.ticketQuota.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AgentTicketQuotaResponseVO {
    private Long id;
    private String concertName;
    private String sessionName;
    private String ticketCategoryName;
    private int totalQuantity;
    private int remainingQuantity;
    private int price;
    private BaseStatus status;

    public int getPrice() {
        return price / 100;
    }
}
