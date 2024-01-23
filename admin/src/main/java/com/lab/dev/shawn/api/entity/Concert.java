package com.lab.dev.shawn.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Concert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    @Where(clause = "is_deleted = false")
    private List<Session> sessionList;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    @Where(clause = "is_deleted = false")
    private List<TicketCategory> ticketCategoryList;

    @JsonIgnore
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    @Where(clause = "is_deleted = false and status = 'ACTIVE' ")
    @OrderBy("id desc")
    private List<Inventory> inventoryList;

    @Enumerated(EnumType.STRING)
    private BaseStatus status;
}
