package com.lab.dev.shawn.admin.business.inventory.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateQuantityRequestVO {
    private Long id;
    private String operation;
    private String quantity;
}
