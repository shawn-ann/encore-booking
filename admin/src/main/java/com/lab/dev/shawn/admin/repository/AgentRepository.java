package com.lab.dev.shawn.admin.repository;

import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.vo.AgentRequestVO;
import com.lab.dev.shawn.admin.vo.AgentResponseVO;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    @Query(value = "SELECT new com.lab.dev.shawn.admin.vo.AgentResponseVO(a.id,a.name,a.mobile,a.createDate) FROM Agent a WHERE a.deleted=false AND a.mobile LIKE %?1%  AND a.name LIKE %?2%")
    Page<AgentResponseVO> findByMobileContainingAndStatus_Active(String mobile,String name, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Override
    <S extends Agent> S save(S entity);
}
