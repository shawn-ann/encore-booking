package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.entity.AgentTicketQuota;
import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaResponseVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentTicketQuotaRepository extends JpaRepository<AgentTicketQuota, Long> {

    @Query("""
            Select new com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaResponseVO(a.id,a.inventory.concert.name,a.inventory.session.name,a.inventory.ticketCategory.name,a.agent.name,a.totalQuantity,a.remainingQuantity,a.price,a.status)
            from AgentTicketQuota a 
            where a.deleted=false 
            and a.inventory.deleted=false
            and (:agentId is NULL or a.agent.id = :agentId) 
            and (:concertId is NULL or a.inventory.concert.id = :concertId)
            """)
    Page<AgentTicketQuotaResponseVO> findByFilter(Long concertId, Long agentId, Pageable pageable);

    @Modifying
    @Query("""
            UPDATE AgentTicketQuota t 
            SET t.status = :toStatus,
            t.version=t.version+1,
            t.updateDate = CURRENT_TIMESTAMP 
            WHERE t.id = :id and t.status=:fromStatus
            """)
    int changeStatus(Long id, BaseStatus fromStatus, BaseStatus toStatus);


    @Modifying
    @Query("""
            UPDATE AgentTicketQuota t 
            SET t.totalQuantity = t.totalQuantity + :quantity,
            t.remainingQuantity = t.remainingQuantity + :quantity,
            t.version=t.version+1,
            t.updateDate = CURRENT_TIMESTAMP 
            WHERE t.id = :id AND t.deleted=false 
            AND t.remainingQuantity + :quantity >= 0
            """)
    int updateQuantity(Long id, int quantity);

    @Query("""
            Select a
            from AgentTicketQuota a 
            where a.deleted=false
            and a.agent.id = :agentId and a.agent.deleted = false and a.agent.status = com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            """)
    List<AgentTicketQuota> findByAgentId(Long agentId);

    @Query("""
            Select a
            from AgentTicketQuota a 
            where a.deleted=false 
            and  a.inventory.id = :inventoryId
            """)
    List<AgentTicketQuota> findByInventoryId(Long inventoryId);

    @Query("""
            Select a
            from AgentTicketQuota a 
            where a.deleted=false and a.status = com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            and a.agent.id = :agentId and a.agent.deleted = false and a.agent.status = com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            """)
    List<AgentTicketQuota> findActivatedByAgentId(Long agentId);

    @Modifying
    @Query("""
            UPDATE AgentTicketQuota t 
            SET
            t.remainingQuantity = t.remainingQuantity + :quantity,
            t.version=t.version+1,
            t.updateDate = CURRENT_TIMESTAMP 
            WHERE t.id = :id AND t.deleted=false 
            AND t.remainingQuantity + :quantity >= 0
            """)
    int addRemainingQuantity(Long id, int quantity);
    @Query("""
            Select a
            from AgentTicketQuota a 
            where a.deleted=false and a.status = com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            and a.agent.id = :agentId and a.agent.status = com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            and a.inventory.concert.id = :concertId and a.inventory.status = com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            """)
    List<AgentTicketQuota> findActivatedByAgentIdAndConcertId(Long agentId, Long concertId);
}
