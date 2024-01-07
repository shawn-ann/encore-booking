package com.lab.dev.shawn.api.entity;

import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

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
    @Where(clause = "is_deleted = false and status=com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE")
    private Inventory inventory;

    private int totalQuantity;
    private int remainingQuantity;
    private int price;

    @Enumerated(EnumType.STRING)
    private BaseStatus status;

}
