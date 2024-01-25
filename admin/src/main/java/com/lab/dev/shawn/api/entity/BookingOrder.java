package com.lab.dev.shawn.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lab.dev.shawn.api.base.constant.OrderStatus;
import com.lab.dev.shawn.api.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class BookingOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_ticket_quota_id")
    @JsonIgnore
    private AgentTicketQuota agentTicketQuota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    @JsonIgnore
    private Agent agent;

    @OneToMany(mappedBy = "bookingOrder", cascade = CascadeType.ALL)
    private List<BookingBuyer> buyerList;

    @OneToMany(mappedBy = "bookingOrder", cascade = CascadeType.ALL)
    @OrderBy("createDate asc")
    private List<BookingOperation> operationList;
    private int buyCount;
    private int buyPrice;
    private String concertName;
    private String sessionName;
    private String ticketCategoryName;
    private int totalFee;
    private String fuiouRefundSsn;
    private String fuiouOrderId;
    private String fuiouPaySsn;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    public String getStatusName(){
        return status.getName();
    }
}
