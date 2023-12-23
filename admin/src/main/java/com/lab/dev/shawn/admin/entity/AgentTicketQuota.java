package com.lab.dev.shawn.admin.entity;

import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class AgentTicketQuota extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private int totalQuantity;
    private int remainingQuantity;
    private int price;

    @Enumerated(EnumType.STRING)
    private BaseStatus status;

}
