package com.lab.dev.shawn.api.wx.order.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.util.JwtUtil;
import com.lab.dev.shawn.api.wx.order.service.WxOrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wx/order")
public class WxOrderController {

    @Autowired
    private WxOrderService wxTicketService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseBody> list(HttpServletRequest request) {

        String token = request.getHeader("X-Token");
        Long agentId = JwtUtil.getTokenClaims(token).get("id", Long.class);

//        List<WxConcertResponseVO> tickets = wxTicketService.findConcertsByAgentId(agentId);
        ApiResponseBody body = new ApiResponseBody(null);
        return ResponseEntity.ok(body);
    }
}
