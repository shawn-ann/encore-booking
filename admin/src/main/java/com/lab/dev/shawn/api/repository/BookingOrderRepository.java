package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.entity.BookingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingOrderRepository extends JpaRepository<BookingOrder, Long> {
    @Query("""
            Select a
            from BookingOrder a 
            where a.deleted=false 
            and  a.agent.id = :agentId
            order by a.createDate desc
            """)
    List<BookingOrder> findByAgentId(Long agentId);

}
