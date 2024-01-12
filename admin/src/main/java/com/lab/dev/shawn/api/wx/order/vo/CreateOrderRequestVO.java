package com.lab.dev.shawn.api.wx.order.vo;


import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequestVO {
    private Long quotaId;
    private List<OrderBuyerVO> buyers;

}