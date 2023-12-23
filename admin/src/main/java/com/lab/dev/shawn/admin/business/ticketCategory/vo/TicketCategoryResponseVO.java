package com.lab.dev.shawn.admin.business.ticketCategory.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TicketCategoryResponseVO {

    private Long id;
    private Long concertId;
    private String concertName;
    private String name;
    private BaseStatus status;

}
