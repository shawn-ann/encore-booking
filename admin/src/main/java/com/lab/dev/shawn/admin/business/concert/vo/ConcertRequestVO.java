package com.lab.dev.shawn.admin.business.concert.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ConcertRequestVO {
    private Long id;
    private String name;

    private List<SessionRequestVO> sessionList;
    private List<TicketCategoryRequestVO> ticketCategoryList;

    @Data
    @AllArgsConstructor
    public static class TicketCategoryRequestVO {
        private String id;
        private String name;
    }
}
