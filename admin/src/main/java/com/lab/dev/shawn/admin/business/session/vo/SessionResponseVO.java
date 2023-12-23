package com.lab.dev.shawn.admin.business.session.vo;

import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionResponseVO {

    private Long id;
    private Long concertId;
    private String concertName;
    private String name;
    private BaseStatus status;

}
