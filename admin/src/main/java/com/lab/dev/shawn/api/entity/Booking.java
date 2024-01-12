package com.lab.dev.shawn.api.entity;

import com.lab.dev.shawn.api.base.constant.OrderStatus;
import com.lab.dev.shawn.api.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_ticket_quota_id")
    private AgentTicketQuota agentTicketQuota;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<BookingBuyer> buyers;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<BookingOperation> operations;
    private int buyCount;
    private int totalFee;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
