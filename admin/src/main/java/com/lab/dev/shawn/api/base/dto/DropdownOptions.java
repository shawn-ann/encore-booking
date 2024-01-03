package com.lab.dev.shawn.api.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DropdownOptions {
    private Long value;
    private String label;
}
