package com.lab.dev.shawn.api.wx.user.service;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.repository.AgentRepository;
import com.lab.dev.shawn.api.component.WeChatApiComponent;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WxUserService {
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private WeChatApiComponent weChatApiUtil;

    public Agent login(String mobile, String wxCode) throws Exception {
        Agent agent = agentRepository.findActiveByMobile(mobile);
        if (agent == null) {
            throw new BaseException(BaseExceptionEnum.USER_NOT_EXIST);
        }
        if (StringUtils.isBlank(agent.getOpenId())) {
            String openId = weChatApiUtil.retrieveOpenId(wxCode);
            agent.setOpenId(openId);
            agentRepository.save(agent);
        }
        return agent;
    }

}
