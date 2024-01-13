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
public class BookingOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_ticket_quota_id")
    private AgentTicketQuota agentTicketQuota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @OneToMany(mappedBy = "bookingOrder", cascade = CascadeType.ALL)
    private List<BookingBuyer> buyerList;

    @OneToMany(mappedBy = "bookingOrder", cascade = CascadeType.ALL)
    private List<BookingOperation> operationList;
    private int buyCount;
    private int buyPrice;
    private String concertName;
    private String sessionName;
    private String ticketCategoryName;
    private int totalFee;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}