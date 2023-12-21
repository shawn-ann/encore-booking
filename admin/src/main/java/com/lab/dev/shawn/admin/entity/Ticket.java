package com.lab.dev.shawn.admin.entity;

import com.lab.dev.shawn.admin.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "ticket")
@Data
@EqualsAndHashCode(callSuper=false)
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    private String area;

    private int totalQuantity;
    
    private int availableQuantity;

    private LocalDate date;


}
