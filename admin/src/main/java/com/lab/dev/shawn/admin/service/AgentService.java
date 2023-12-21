package com.lab.dev.shawn.admin.service;

import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.repository.AgentRepository;
import com.lab.dev.shawn.admin.util.ServiceUtil;
import com.lab.dev.shawn.admin.vo.AgentRequestVO;
import com.lab.dev.shawn.admin.vo.AgentResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {


    private AgentRepository agentRepository;

    @Autowired
    public void setAgentRepository(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> findById(Long userId) {
        return agentRepository.findById(userId).stream().toList();
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
        ServiceUtil.handleCreate(agent);
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
        agent.setStatus(BaseStatus.INACTIVE);
        agentRepository.save(agent);
    }
}
