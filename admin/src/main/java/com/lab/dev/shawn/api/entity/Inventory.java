package com.lab.dev.shawn.api.entity;

import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "ticket_category_id", "is_deleted"}))
@EqualsAndHashCode(callSuper = false)
public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalQuantity;

    private Integer remainingQuantity;

    private Integer unallocatedQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    @Where(clause = "is_deleted = false and status=com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE")
    private Concert concert;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_category_id")
    private TicketCategory ticketCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Enumerated(EnumType.STRING)
    private BaseStatus status;

}
