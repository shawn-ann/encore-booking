package com.lab.dev.shawn.admin.controller;

import com.lab.dev.shawn.admin.base.dto.ApiResponseBody;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.entity.User;
import com.lab.dev.shawn.admin.service.AgentService;
import com.lab.dev.shawn.admin.service.UserService;
import com.lab.dev.shawn.admin.util.JwtUtil;
import com.lab.dev.shawn.admin.vo.AgentVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/agent")
public class AgentController {
    private AgentService agentService;

    @Autowired
    public void setAgentService(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseBody> list(int page, int limit, String mobile) {
        Page<AgentVO> agents = agentService.findByMobile(page, limit, mobile);
        ApiResponseBody body = new ApiResponseBody(agents);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseBody> create(@RequestBody AgentVO agentVO) {
        agentService.create(agentVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponseBody> update(@RequestBody AgentVO agentVO) throws BaseException {
        agentService.update(agentVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseBody> delete(@PathVariable("id") Long id) throws BaseException {
        agentService.delete(id);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }
}
