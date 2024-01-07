package com.lab.dev.shawn.api.wx.ticket.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.util.JwtUtil;
import com.lab.dev.shawn.api.wx.ticket.service.WxTicketService;
import com.lab.dev.shawn.api.wx.ticket.vo.WxConcertResponseVO;
import com.lab.dev.shawn.api.wx.ticket.vo.WxTicketResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/wx")
public class WxTicketController {

    @Autowired
    private WxTicketService wxTicketService;

    @GetMapping("/concerts")
    public ResponseEntity<ApiResponseBody> concerts(HttpServletRequest request) {

        String token = request.getHeader("X-Token");
        Long agentId = JwtUtil.getTokenClaims(token).get("id", Long.class);

        List<WxConcertResponseVO> concerts = wxTicketService.findConcertsByAgentId(agentId);
        ApiResponseBody body = new ApiResponseBody(concerts);
        return ResponseEntity.ok(body);
    }


    @GetMapping("/tickets/{concertId}")
    public ResponseEntity<ApiResponseBody> tickets(@PathVariable("concertId") String concertId, HttpServletRequest request) {

        String token = request.getHeader("X-Token");
        Long agentId = JwtUtil.getTokenClaims(token).get("id", Long.class);

        WxTicketResponseVO tickets = wxTicketService.findTicketsByAgentIdAndConcertId(agentId, Long.valueOf(concertId));
        ApiResponseBody body = new ApiResponseBody(tickets);
        return ResponseEntity.ok(body);
    }
}
