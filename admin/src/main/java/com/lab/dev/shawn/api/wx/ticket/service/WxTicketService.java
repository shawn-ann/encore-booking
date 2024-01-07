package com.lab.dev.shawn.api.wx.ticket.service;

import com.lab.dev.shawn.api.entity.AgentTicketQuota;
import com.lab.dev.shawn.api.entity.Concert;
import com.lab.dev.shawn.api.repository.AgentTicketQuotaRepository;
import com.lab.dev.shawn.api.repository.ConcertRepository;
import com.lab.dev.shawn.api.wx.ticket.vo.WxConcertResponseVO;
import com.lab.dev.shawn.api.wx.ticket.vo.WxTicketResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class WxTicketService {
    @Autowired
    private AgentTicketQuotaRepository agentTicketQuotaRepository;
    @Autowired
    private ConcertRepository concertRepository;

    public List<WxConcertResponseVO> findConcertsByAgentId(Long agentId) {
        List<AgentTicketQuota> quotas = agentTicketQuotaRepository.findActivatedByAgentId(agentId);
        return quotas.stream().map(item -> item.getInventory().getConcert())
                .distinct().map(item -> new WxConcertResponseVO(item.getId(), item.getName())).collect(Collectors.toList());
    }

    public WxTicketResponseVO findTicketsByAgentIdAndConcertId(Long agentId, Long concertId) {
        Concert concert = concertRepository.findById(concertId).get();

        WxTicketResponseVO vo = new WxTicketResponseVO();
        vo.setName(concert.getName());

        List<AgentTicketQuota> quotas = agentTicketQuotaRepository.findActivatedByAgentIdAndConcertId(agentId, concertId);

        List<Map<String, Object>> sessions = concert.getSessionList().stream().map(item -> {
            Map<String, Object> session = new HashMap<>();
            session.put("id", item.getId());
            session.put("name", item.getName());
            List<Map<String, Object>> tickets = quotas.stream().filter(quota -> item.getId().equals(quota.getInventory().getSession().getId())).map(quota -> {
                Map<String, Object> ticket = new HashMap<>();
                ticket.put("ticketCategoryId", quota.getInventory().getTicketCategory().getId());
                ticket.put("ticketCategoryName", quota.getInventory().getTicketCategory().getName());
                ticket.put("quotaId", quota.getId());
                ticket.put("remainingQuantity", quota.getRemainingQuantity());
                ticket.put("price", quota.getPrice());
                return ticket;
            }).collect(Collectors.toList());
            session.put("tickets", tickets);
            return session;
        }).collect(Collectors.toList());

        vo.setSessions(sessions);
        return vo;
    }
}
