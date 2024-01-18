package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.entity.BookingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
            Select 
            a
            from BookingOrder a 
            where a.deleted=false 
            and (:agentMobile is NULL or a.agent.mobile LIKE %:agentMobile%) 
            and (:concertName is NULL or a.concertName LIKE %:concertName%) 
            and (:beginDate is NULL or a.createDate >= :beginDate) 
            and (:endDate is NULL or a.concertName <= :endDate) 
            order by a.createDate desc
            """)
    Page<BookingOrder> findByConcertNameAndAgentMobile(String concertName, String agentMobile, String beginDate, String endDate, Pageable pageable);


    @Query("""
            Select 
            a
            from BookingOrder a 
            where a.id = :id 
            """)
    BookingOrder findByID(Long id);

    @Query("""
            Select 
            a
            from BookingOrder a 
            where a.orderNumber = :orderNumber 
            """)
    BookingOrder findByOrderNumber(String orderNumber);
}
