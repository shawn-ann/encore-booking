package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.admin.inventory.vo.InventoryResponseVO;
import com.lab.dev.shawn.api.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    @Query("""
            SELECT 
            new com.lab.dev.shawn.api.admin.inventory.vo.InventoryResponseVO(a.id,a.concert.name AS concertName,a.session.name sessionName,a.ticketCategory.name ticketCategoryName,a.totalQuantity,a.unallocatedQuantity,a.remainingQuantity,a.status) 
            FROM Inventory a
            WHERE a.deleted=false 
            and (:concertId is null or a.concert.id = :concertId)
            order by a.concert.name,a.session.name,a.ticketCategory.name
            """)
    Page<InventoryResponseVO> findByFilter(Long concertId, Pageable pageable);

    @Modifying
    @Query("""
            UPDATE Inventory t SET t.totalQuantity = t.totalQuantity + :quantity,
            t.remainingQuantity = t.remainingQuantity + :quantity,
            t.unallocatedQuantity = t.unallocatedQuantity + :quantity,
            t.version=t.version+1,
            t.updateDate = CURRENT_TIMESTAMP 
            WHERE t.id = :id AND t.deleted=false 
            AND t.remainingQuantity + :quantity >= 0
            AND t.unallocatedQuantity + :quantity >= 0
            """)
    int updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);

    @Modifying
    @Query("""
            UPDATE Inventory t 
            SET t.status = :toStatus,
            t.version=t.version+1,
            t.updateDate = CURRENT_TIMESTAMP 
            WHERE t.id = :id and t.status=:fromStatus
            """)
    int changeStatus(Long id, BaseStatus fromStatus, BaseStatus toStatus);

    @Query("""
            SELECT 
            a
            FROM Inventory a
            WHERE a.deleted=false 
            and a.concert.id = :concertId
            and a.session.id = :sessionId
            and a.ticketCategory.id = :ticketCategoryId
            """)
    Inventory findByConcertIdAndSessionIdAndTicketCategoryId(Long concertId, Long sessionId, Long ticketCategoryId);

    @Query(value = """
            select new com.lab.dev.shawn.api.base.dto.DropdownOptions(a.id,concat(a.session.name, a.ticketCategory.name,' - 剩余库存：', a.unallocatedQuantity)) 
            from Inventory a 
            where a.deleted=false 
            and a.status=com.lab.dev.shawn.api.base.constant.BaseStatus.ACTIVE
            and a.concert.id = :concertId
            order by a.id asc
            """)
    List<DropdownOptions> findDropdownOptions(Long concertId);

    @Modifying
    @Query("""
            UPDATE Inventory t 
            SET
            t.unallocatedQuantity = t.unallocatedQuantity + :quantity,
            t.version=t.version+1,
            t.updateDate = CURRENT_TIMESTAMP 
            WHERE t.id = :id AND t.deleted=false 
            AND t.unallocatedQuantity + :quantity >= 0
            """)
    int addUnallocatedQuantity(Long id, int quantity);

    @Modifying
    @Query("""
            UPDATE Inventory t 
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
            from Inventory a 
            where a.deleted=false 
            and  a.session.id IN :sessionIds
            """)
    List<Inventory> findBySessionIds(List<Long> sessionIds);

    @Query("""
            Select a
            from Inventory a 
            where a.deleted=false 
            and  a.ticketCategory.id IN :ticketCategoryIds
            """)
    List<Inventory> findByTicketCategoryIds(List<Long> ticketCategoryIds);

    @Query("""
            Select a
            from Inventory a 
            where a.deleted=false 
            and  a.concert.id = :concertId
            """)
    List<Inventory> findByConcertId(Long concertId);
}
