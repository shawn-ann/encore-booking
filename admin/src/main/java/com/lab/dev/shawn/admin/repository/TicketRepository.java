package com.lab.dev.shawn.admin.repository;

import com.lab.dev.shawn.admin.entity.Concert;
import com.lab.dev.shawn.admin.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
