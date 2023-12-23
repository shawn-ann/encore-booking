package com.lab.dev.shawn.admin.business.session.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionRequestVO {
    private Long id;
    private Long concertId;
    private String name;
}
