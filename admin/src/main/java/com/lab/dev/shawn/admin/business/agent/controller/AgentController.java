package com.lab.dev.shawn.admin.business.agent.controller;

import com.lab.dev.shawn.admin.base.dto.ApiResponseBody;
import com.lab.dev.shawn.admin.base.dto.DropdownOptions;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.business.agent.service.AgentService;
import com.lab.dev.shawn.admin.business.agent.vo.AgentRequestVO;
import com.lab.dev.shawn.admin.business.agent.vo.AgentResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/agent")
public class AgentController {
    private AgentService agentService;

    @Autowired
    public void setAgentService(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseBody> list(int page, int limit, String mobile, String name) {
        Page<AgentResponseVO> agents = agentService.findByMobile(page, limit, mobile, name);
        ApiResponseBody body = new ApiResponseBody(agents);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseBody> create(@RequestBody AgentRequestVO agentRequestVO) {
        agentService.create(agentRequestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponseBody> update(@RequestBody AgentRequestVO agentRequestVO) throws BaseException {
        agentService.update(agentRequestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseBody> delete(@PathVariable("id") String id) throws BaseException {
        agentService.delete(Long.valueOf(id));
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }


    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponseBody> dropdown() {
        List<DropdownOptions> dateDropdownOptions = agentService.findDropdownOptions();
        ApiResponseBody body = new ApiResponseBody(dateDropdownOptions);
        return ResponseEntity.ok(body);
    }
}
