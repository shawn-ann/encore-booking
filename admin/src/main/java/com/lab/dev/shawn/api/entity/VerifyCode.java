package com.lab.dev.shawn.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class VerifyCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobile;
    private String code;
    private boolean isLogin;
    private boolean verified;

    @CreatedDate
    private LocalDateTime createDate;
    private LocalDateTime expiredDate;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        expiredDate = LocalDateTime.now().plusMinutes(5);
        verified = false;
    }
}
