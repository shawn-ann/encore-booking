package com.lab.dev.shawn.admin.base.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PaginationEntity<T> {
    public PaginationEntity(int total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    private int total;
    private List<T> items;

}
