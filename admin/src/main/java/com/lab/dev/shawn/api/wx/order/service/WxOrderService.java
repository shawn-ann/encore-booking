package com.lab.dev.shawn.api.wx.order.service;

import com.lab.dev.shawn.api.repository.AgentTicketQuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WxOrderService {
    @Autowired
    private AgentTicketQuotaRepository agentTicketQuotaRepository;

    public CreateOrderResponseDTO create(CreateOrderRequestVO requestVO,Long agentId) {

        //查询配额

        // 判断该代理配额是否充足


        // 扣除配额表的库存和库存表的库存
        
        //创建订单信息

    
        CreateOrderResponseDTO response = new CreateOrderResponseDTO;
        return response;
    }

}
