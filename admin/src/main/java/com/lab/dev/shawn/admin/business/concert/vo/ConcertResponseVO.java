package com.lab.dev.shawn.admin.business.concert.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lab.dev.shawn.admin.business.session.vo.SessionResponseVO;
import com.lab.dev.shawn.admin.business.ticketCategory.vo.TicketCategoryResponseVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ConcertResponseVO {
    private Long id;
    private String name;
    private List<String> sessionList;
//    private List<TicketCategoryResponseVO> ticketCategoryList;
}
