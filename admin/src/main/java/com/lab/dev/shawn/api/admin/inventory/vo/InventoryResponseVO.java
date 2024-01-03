package com.lab.dev.shawn.api.admin.inventory.vo;

import com.lab.dev.shawn.api.base.constant.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryResponseVO {

    private Long id;
    private String concertName;
    private String sessionName;
    private String ticketCategoryName;
    private int totalQuantity;
    private int unallocatedQuantity;
    private int remainingQuantity;
    private BaseStatus status;

}
