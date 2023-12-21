package com.lab.dev.shawn.admin.base.entity;

import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @Version
    private int version;
    @Column(name = "is_deleted")
    private boolean deleted;
}
