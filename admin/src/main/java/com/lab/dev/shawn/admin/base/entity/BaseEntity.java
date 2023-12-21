package com.lab.dev.shawn.admin.base.entity;

import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

    @Enumerated(EnumType.STRING)
    private BaseStatus status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @Version
    private int version;
}
