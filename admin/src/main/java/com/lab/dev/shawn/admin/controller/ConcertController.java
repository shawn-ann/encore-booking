package com.lab.dev.shawn.admin.controller;

import com.lab.dev.shawn.admin.base.dto.ApiResponseBody;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.service.AgentService;
import com.lab.dev.shawn.admin.service.ConcertService;
import com.lab.dev.shawn.admin.vo.AgentRequestVO;
import com.lab.dev.shawn.admin.vo.AgentResponseVO;
import com.lab.dev.shawn.admin.vo.ConcertRequestVO;
import com.lab.dev.shawn.admin.vo.ConcertResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/concert")
public class ConcertController {
    private ConcertService concertService;

    @Autowired
    public void setConcertService(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseBody> list(int page, int limit, String name) {
        Page<ConcertResponseVO> agents = concertService.findByMobile(page, limit, name);
        ApiResponseBody body = new ApiResponseBody(agents);
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
}
