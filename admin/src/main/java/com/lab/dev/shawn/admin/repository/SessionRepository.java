package com.lab.dev.shawn.admin.repository;

import com.lab.dev.shawn.admin.base.dto.DropdownOptions;
import com.lab.dev.shawn.admin.business.session.vo.SessionResponseVO;
import com.lab.dev.shawn.admin.business.ticketCategory.vo.TicketCategoryResponseVO;
import com.lab.dev.shawn.admin.entity.AgentTicketQuota;
import com.lab.dev.shawn.admin.entity.Inventory;
import com.lab.dev.shawn.admin.entity.Session;
import com.lab.dev.shawn.admin.entity.TicketCategory;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

    @Query(value = """
            SELECT 
            new com.lab.dev.shawn.admin.business.session.vo.SessionResponseVO(a.id,a.concert.id AS concertId,a.concert.name AS concertName,a.name,a.status) 
            FROM Session a
            WHERE a.deleted=false 
            and (:concertId is null or a.concert.id = :concertId)
            order by a.name desc 
            """)
    Page<SessionResponseVO> findByFilter(Long concertId, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    TicketCategory save(TicketCategory entity);

    @Query(value = """
            select new com.lab.dev.shawn.admin.base.dto.DropdownOptions(a.id,a.name) 
            from Session a 
            where a.deleted=false 
            and a.status=com.lab.dev.shawn.admin.base.constant.BaseStatus.ACTIVE
            and a.concert.id = :concertId
            order by a.id asc
            """)
    List<DropdownOptions> findDropdownOptions(Long concertId);

    @Query("""
            Select a
            from Session a 
            where a.deleted=false 
            and  a.concert.id = :concertId
            """)
    List<Session> findByConcertId(Long concertId);
}
