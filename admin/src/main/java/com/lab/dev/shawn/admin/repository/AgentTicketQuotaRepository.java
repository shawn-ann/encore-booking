package com.lab.dev.shawn.admin.repository;

import com.lab.dev.shawn.admin.entity.AgentTicketQuota;
import com.lab.dev.shawn.admin.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentTicketQuotaRepository extends JpaRepository<AgentTicketQuota, Long> {
}
