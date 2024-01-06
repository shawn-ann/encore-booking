package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.admin.agent.vo.AgentResponseVO;
import com.lab.dev.shawn.api.entity.AgentTicketQuota;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    @Query(value = """
            SELECT new com.lab.dev.shawn.api.admin.agent.vo.AgentResponseVO(a.id,a.name,a.mobile,a.createDate) 
            FROM Agent a WHERE a.deleted=false AND a.mobile LIKE %?1%  AND a.name LIKE %?2%
            """)
    Page<AgentResponseVO> findByMobileContainingAndStatus_Active(String mobile, String name, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Override
    <S extends Agent> S save(S entity);


    @Query(value = """
            select new com.lab.dev.shawn.api.base.dto.DropdownOptions(a.id,a.name) 
            from Agent a 
            where a.deleted=false 
            and a.status=com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            """)
    List<DropdownOptions> findDropdownOptions();

    @Query("""
            SELECT 
            a
            FROM AgentTicketQuota a
            WHERE a.deleted=false 
            and a.agent.id = :agentId
            and a.inventory.id = :inventoryId
            """)
    AgentTicketQuota findByAgentIdAndInventoryId(Long agentId, Long inventoryId);

    @Query("""
            SELECT 
            a
            FROM Agent a
            WHERE a.deleted=false 
            and a.status=com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            and a.mobile = :mobile
            """)
    Agent findActiveByMobile(String mobile);

    @Query("""
            SELECT 
            a
            FROM Agent a
            WHERE a.deleted=false 
            and a.mobile = :mobile
            """)
    Agent findByMobile(String mobile);


}
