package com.lab.dev.shawn.api.wx.ticket.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WxTicketResponseVO {
    private String name;
    List<Map<String, Object>> sessions;

}
