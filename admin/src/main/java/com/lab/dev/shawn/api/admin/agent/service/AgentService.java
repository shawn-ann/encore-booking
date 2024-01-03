package com.lab.dev.shawn.api.admin.agent.service;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.repository.AgentRepository;
import com.lab.dev.shawn.api.admin.agent.vo.AgentRequestVO;
import com.lab.dev.shawn.api.admin.agent.vo.AgentResponseVO;
import com.lab.dev.shawn.api.repository.AgentTicketQuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AgentService {

    private AgentRepository agentRepository;
    @Autowired
    private AgentTicketQuotaRepository agentTicketQuotaRepository;

    @Autowired
    public void setAgentRepository(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public Page<AgentResponseVO> findByMobile(int page, int limit, String mobile, String name) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return agentRepository.findByMobileContainingAndStatus_Active(mobile, name, pageable);
    }

    public void create(AgentRequestVO agentRequestVO) {
        Agent agent = new Agent();
        agent.setName(agentRequestVO.getName());
        agent.setMobile(agentRequestVO.getMobile());
        agent.setStatus(BaseStatus.ACTIVE);
        agentRepository.save(agent);
    }

    public void update(AgentRequestVO agentRequestVO) throws BaseException {
        Agent agent = agentRepository.findById(agentRequestVO.getId()).get();
        if (agent == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (agent.getStatus().equals(BaseStatus.INACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        agent.setMobile(agentRequestVO.getMobile());
        agent.setName(agentRequestVO.getName());
        agentRepository.save(agent);
    }

    public void delete(Long id) throws BaseException {
        Agent agent = agentRepository.findById(id).get();
        if (agent == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (agent.getStatus().equals(BaseStatus.INACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        if (!agentTicketQuotaRepository.findByAgentId(id).isEmpty()) {
            throw new BaseException(50008, "请先删除配额再删除该记录");
        }
        agent.setDeleted(true);
        agentRepository.save(agent);
    }

    public List<DropdownOptions> findDropdownOptions() {
        return agentRepository.findDropdownOptions();
    }
}
