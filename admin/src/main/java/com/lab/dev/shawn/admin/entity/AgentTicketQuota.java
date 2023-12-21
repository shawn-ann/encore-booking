package com.lab.dev.shawn.admin.entity;

import com.lab.dev.shawn.admin.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "agent_ticket_quota")
@Data
@EqualsAndHashCode(callSuper=false)
public class AgentTicketQuota  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private int quantity;



}
