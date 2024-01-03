package com.lab.dev.shawn.api.admin.agent.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentRequestVO {
    private Long id;
    private String mobile;
    private String name;
}
