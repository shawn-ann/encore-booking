package com.lab.dev.shawn.api.entity;

import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "agent_ticket_quota_id")
    private AgentTicketQuota quota;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Where(clause = "is_deleted = false")
    private List<OrderUser> users;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
