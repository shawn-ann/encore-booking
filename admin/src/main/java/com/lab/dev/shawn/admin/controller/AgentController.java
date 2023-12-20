package com.lab.dev.shawn.admin.controller;

import com.lab.dev.shawn.admin.base.dto.ApiResponseBody;
import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.entity.User;
import com.lab.dev.shawn.admin.service.AgentService;
import com.lab.dev.shawn.admin.service.UserService;
import com.lab.dev.shawn.admin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AgentController {
    private AgentService agentService;

    @Autowired
    public void setAgentService(AgentService agentService) {
        this.agentService = agentService;
    }


    @GetMapping("/agents")
    public ResponseEntity agents() {
        List<Agent> user = agentService.findAll();

        ApiResponseBody body = new ApiResponseBody(user);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/transaction/list")
    public ResponseEntity list() {
        ApiResponseBody body = new ApiResponseBody(new ArrayList<String>());
        return ResponseEntity.ok(body);
    }
}
