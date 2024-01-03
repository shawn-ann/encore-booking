package com.lab.dev.shawn.api.admin.ticketCategory.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.admin.ticketCategory.service.TicketCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/ticket_category")
public class TicketCategoryController {
    private TicketCategoryService ticketCategoryService;

    @Autowired
    public void setTicketCategoryService(TicketCategoryService ticketCategoryService) {
        this.ticketCategoryService = ticketCategoryService;
    }


    @GetMapping("/dropdown/{concertId}")
    public ResponseEntity<ApiResponseBody> dateDropdown(@PathVariable("concertId") Long concertId) {
        List<DropdownOptions> dateDropdownOptions = ticketCategoryService.findDropdownOptions(concertId);
        ApiResponseBody body = new ApiResponseBody(dateDropdownOptions);
        return ResponseEntity.ok(body);
    }
}
