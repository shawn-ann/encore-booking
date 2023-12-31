package com.lab.dev.shawn.api.admin.concert.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.admin.concert.service.ConcertService;
import com.lab.dev.shawn.api.admin.concert.vo.ConcertRequestVO;
import com.lab.dev.shawn.api.entity.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/concert")
public class ConcertController {
    private ConcertService concertService;

    @Autowired
    public void setConcertService(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseBody> list(int page, int limit, String name) {
        Page<Concert> concerts = concertService.findByName(page, limit, name);
        ApiResponseBody body = new ApiResponseBody(concerts);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseBody> create(@RequestBody ConcertRequestVO requestVO) {
        concertService.create(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponseBody> update(@RequestBody ConcertRequestVO requestVO) throws BaseException {
        concertService.update(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseBody> delete(@PathVariable("id") String id) throws BaseException {
        concertService.delete(Long.valueOf(id));
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponseBody> dropdown() {
        List<DropdownOptions> dateDropdownOptions = concertService.findDropdownOptions();
        ApiResponseBody body = new ApiResponseBody(dateDropdownOptions);
        return ResponseEntity.ok(body);
    }
}
