package com.lab.dev.shawn.api.admin.bookingOrder.controller;

import com.lab.dev.shawn.api.admin.bookingOrder.dto.DetailBookingOrderResponseDTO;
import com.lab.dev.shawn.api.admin.bookingOrder.service.BookingOrderService;
import com.lab.dev.shawn.api.admin.bookingOrder.vo.SearchBookingOrderRequestVO;
import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.entity.BookingOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/booking_order")
public class BookingOrderController {
    @Autowired
    private BookingOrderService bookingOrderService;


    @PostMapping("/list")
    public ResponseEntity<ApiResponseBody> list(@RequestBody SearchBookingOrderRequestVO requestVO) {
        Page<BookingOrder> results = bookingOrderService.list(requestVO);
        ApiResponseBody body = new ApiResponseBody(results);
        return ResponseEntity.ok(body);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponseBody> detail(@PathVariable Long id) {
        DetailBookingOrderResponseDTO results = bookingOrderService.detail(id);
        ApiResponseBody body = new ApiResponseBody(results);
        return ResponseEntity.ok(body);
    }

}
