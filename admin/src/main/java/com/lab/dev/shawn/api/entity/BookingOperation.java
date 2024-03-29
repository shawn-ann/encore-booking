package com.lab.dev.shawn.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lab.dev.shawn.api.base.constant.OperationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class BookingOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OperationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_order_id")
    @JsonIgnore
    private BookingOrder bookingOrder;

    @CreatedDate
    private LocalDateTime createDate;
    private String note;
    private String additionalInfo;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

    public String getStatusName() {
        return status.getName();
    }
}
