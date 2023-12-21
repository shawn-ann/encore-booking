package com.lab.dev.shawn.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AgentRequestVO {
    private Long id;
    private String mobile;
    private String name;
}
