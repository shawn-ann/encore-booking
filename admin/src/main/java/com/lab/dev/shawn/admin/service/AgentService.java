package com.lab.dev.shawn.admin.service;

import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.dto.PaginationEntity;
import com.lab.dev.shawn.admin.base.entity.BaseEntity;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.entity.User;
import com.lab.dev.shawn.admin.repository.AgentRepository;
import com.lab.dev.shawn.admin.repository.UserRepository;
import com.lab.dev.shawn.admin.util.ServiceUtil;
import com.lab.dev.shawn.admin.vo.AgentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

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

    public Page<AgentVO> findByMobile(int page, int limit, String mobile) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return agentRepository.findByMobileContainingAndStatus_Active(mobile, pageable);
    }

    public void create(AgentVO agentVO) {
        Agent agent = new Agent();
        agent.setName(agentVO.getName());
        agent.setMobile(agentVO.getMobile());
        ServiceUtil.handleCreate(agent);
        agentRepository.save(agent);
    }

    public void update(AgentVO agentVO) throws BaseException {
        Agent agent = agentRepository.findById(agentVO.getId()).get();
        if (agent == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (agent.getStatus().equals(BaseStatus.INACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        agent.setMobile(agentVO.getMobile());
        agent.setName(agentVO.getName());
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
