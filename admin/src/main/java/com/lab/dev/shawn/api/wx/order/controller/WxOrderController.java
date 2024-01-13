package com.lab.dev.shawn.api.wx.order.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.util.JwtUtil;
import com.lab.dev.shawn.api.wx.order.dto.CreateOrderResponseDTO;
import com.lab.dev.shawn.api.wx.order.service.WxOrderService;
import com.lab.dev.shawn.api.wx.order.vo.CreateOrderRequestVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/wx/order")
public class WxOrderController {

    @Autowired
    private WxOrderService wxOrderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseBody> create(@RequestBody CreateOrderRequestVO requestVO, HttpServletRequest request) throws BaseException {

        String token = request.getHeader("X-Token");
        Long agentId = JwtUtil.getTokenClaims(token).get("id", Long.class);

        CreateOrderResponseDTO response = wxOrderService.create(requestVO, agentId);
        ApiResponseBody body = new ApiResponseBody(response);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/pay")
    public ResponseEntity<ApiResponseBody> pay(@RequestBody String orderId, HttpServletRequest request) throws BaseException {

        String token = request.getHeader("X-Token");
        Long agentId = JwtUtil.getTokenClaims(token).get("id", Long.class);

        ApiResponseBody body = new ApiResponseBody(null);
        return ResponseEntity.ok(body);
    }


    @GetMapping("/list")
    public ResponseEntity<ApiResponseBody> list(HttpServletRequest request) throws BaseException {

        String token = request.getHeader("X-Token");
        Long agentId = JwtUtil.getTokenClaims(token).get("id", Long.class);

        List<HashMap<String, Object>> response = wxOrderService.list(agentId);
        ApiResponseBody body = new ApiResponseBody(response);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponseBody> detail(@PathVariable("id") Long orderId, HttpServletRequest request) throws BaseException {

        String token = request.getHeader("X-Token");
        Long agentId = JwtUtil.getTokenClaims(token).get("id", Long.class);

        HashMap<String, Object> response = wxOrderService.orderDetail(orderId, agentId);
        ApiResponseBody body = new ApiResponseBody(response);
        return ResponseEntity.ok(body);
    }
}
