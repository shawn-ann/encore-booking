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

}
