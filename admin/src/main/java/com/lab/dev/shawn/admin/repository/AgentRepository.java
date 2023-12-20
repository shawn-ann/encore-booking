package com.lab.dev.shawn.admin.repository;

import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
}
