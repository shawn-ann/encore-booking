package com.lab.dev.shawn.api.admin.ticketQuota.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.admin.ticketQuota.service.AgentTicketQuotaService;
import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaQuantityUpdateRequestVO;
import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaRequestVO;
import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaResponseVO;
import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaUpdateRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/quota")
public class AgentTicketQuotaController {
    private AgentTicketQuotaService agentTicketQuotaService;

    @Autowired
    public void setAgentTicketQuotaService(AgentTicketQuotaService agentTicketQuotaService) {
        this.agentTicketQuotaService = agentTicketQuotaService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseBody> list(int page, int limit, Long concertId, Long agentId) {
        Page<AgentTicketQuotaResponseVO> tickets = agentTicketQuotaService.findByFilter(page, limit, concertId, agentId);
        ApiResponseBody body = new ApiResponseBody(tickets);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseBody> create(@RequestBody AgentTicketQuotaRequestVO requestVO) throws BaseException {
        agentTicketQuotaService.create(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponseBody> update(@RequestBody AgentTicketQuotaUpdateRequestVO requestVO) throws BaseException {
        agentTicketQuotaService.update(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseBody> delete(@PathVariable("id") String id) throws BaseException {
        agentTicketQuotaService.delete(Long.valueOf(id));
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/quantity")
    public ResponseEntity<ApiResponseBody> quantityUpdate(@RequestBody AgentTicketQuotaQuantityUpdateRequestVO requestVO) throws BaseException {
        agentTicketQuotaService.quantityUpdate(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/active/{id}")
    public ResponseEntity<ApiResponseBody> active(@PathVariable("id") String id) throws BaseException {
        agentTicketQuotaService.active(Long.valueOf(id));
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/inactive/{id}")
    public ResponseEntity<ApiResponseBody> inactive(@PathVariable("id") String id) throws BaseException {
        agentTicketQuotaService.inactive(Long.valueOf(id));
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

}
