package com.lab.dev.shawn.admin.service;

import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.entity.User;
import com.lab.dev.shawn.admin.repository.AgentRepository;
import com.lab.dev.shawn.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class AgentService {


    private AgentRepository agentRepository;


    public List<Agent> findById(Long userId) {
        return agentRepository.findById(userId).stream().toList();
    }

    public List<Agent> findAll() {
        return agentRepository.findAll();
    }
}
