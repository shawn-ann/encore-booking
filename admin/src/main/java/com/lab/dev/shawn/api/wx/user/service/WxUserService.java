package com.lab.dev.shawn.api.wx.user.service;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WxUserService {
    @Autowired
    private AgentRepository agentRepository;

    public Agent login(String mobile) throws BaseException {
        Agent agent = agentRepository.findActiveByMobile(mobile);
        if (agent == null) {
            throw new BaseException(BaseExceptionEnum.USER_NOT_EXIST);
        }
        return agent;
    }

}
