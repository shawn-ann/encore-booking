package com.lab.dev.shawn.api.entity;

import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class OrderUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String idCardNumber;
    private String mobile;
}
