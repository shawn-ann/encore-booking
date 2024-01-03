package com.lab.dev.shawn.api.admin.concert.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ConcertResponseVO {
    private Long id;
    private String name;
    private List<String> sessionList;
}
