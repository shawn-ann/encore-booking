package com.lab.dev.shawn.api.admin.session.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.admin.session.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/session")
public class SessionController {
    private SessionService sessionService;

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @GetMapping("/dropdown/{concertId}")
    public ResponseEntity<ApiResponseBody> dateDropdown(@PathVariable("concertId") Long concertId) {
        List<DropdownOptions> dateDropdownOptions = sessionService.findDropdownOptions(concertId);
        ApiResponseBody body = new ApiResponseBody(dateDropdownOptions);
        return ResponseEntity.ok(body);
    }
}
