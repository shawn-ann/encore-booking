package com.lab.dev.shawn.admin.business.ticketCategory.controller;

import com.lab.dev.shawn.admin.base.dto.ApiResponseBody;
import com.lab.dev.shawn.admin.base.dto.DropdownOptions;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.business.concert.vo.ConcertRequestVO;
import com.lab.dev.shawn.admin.business.ticketCategory.service.TicketCategoryService;
import com.lab.dev.shawn.admin.business.ticketCategory.vo.TicketCategoryResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ticket_category")
public class TicketCategoryController {
    private TicketCategoryService ticketCategoryService;

    @Autowired
    public void setTicketCategoryService(TicketCategoryService ticketCategoryService) {
        this.ticketCategoryService = ticketCategoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseBody> list(int page, int limit, Long concertId) {
        Page<TicketCategoryResponseVO> tickets = ticketCategoryService.findByFilter(page, limit, concertId);
        ApiResponseBody body = new ApiResponseBody(tickets);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseBody> create(@RequestBody ConcertRequestVO.TicketCategoryRequestVO requestVO) {
        ticketCategoryService.create(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponseBody> update(@RequestBody ConcertRequestVO.TicketCategoryRequestVO requestVO) throws BaseException {
        ticketCategoryService.update(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseBody> delete(@PathVariable("id") String id) throws BaseException {
        ticketCategoryService.delete(Long.valueOf(id));
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/dropdown/{concertId}")
    public ResponseEntity<ApiResponseBody> dateDropdown(@PathVariable("concertId") Long concertId) {
        List<DropdownOptions> dateDropdownOptions = ticketCategoryService.findDropdownOptions(concertId);
        ApiResponseBody body = new ApiResponseBody(dateDropdownOptions);
        return ResponseEntity.ok(body);
    }
}
