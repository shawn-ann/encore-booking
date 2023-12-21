package com.lab.dev.shawn.admin.repository;

import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.entity.User;
import com.lab.dev.shawn.admin.vo.AgentVO;
import jakarta.persistence.LockModeType;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    @Query(value = "SELECT new com.lab.dev.shawn.admin.vo.AgentVO(a.id,a.name,a.mobile,a.createDate) FROM Agent a WHERE a.status='ACTIVE' AND a.mobile LIKE %?1%")
    Page<AgentVO> findByMobileContainingAndStatus_Active(String mobile, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Override
    <S extends Agent> S save(S entity);
}
