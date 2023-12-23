package com.lab.dev.shawn.admin.repository;

import com.lab.dev.shawn.admin.base.dto.DropdownOptions;
import com.lab.dev.shawn.admin.entity.Concert;
import com.lab.dev.shawn.admin.business.concert.vo.ConcertResponseVO;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends CrudRepository<Concert, Long> {
    @Query(value = """
            SELECT new com.lab.dev.shawn.admin.business.concert.vo.ConcertResponseVO(a.id,a.name,a.createDate) 
            FROM Concert a 
            WHERE a.deleted=false AND a.name LIKE %?1%
            """)
    Page<ConcertResponseVO> findByName(String name, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Override
    <S extends Concert> S save(S entity);

    @Query(value = """
            select new com.lab.dev.shawn.admin.base.dto.DropdownOptions(a.id,a.name) 
            from Concert a 
            where a.deleted=false 
            and a.status=com.lab.dev.shawn.admin.base.constant.BaseStatus.ACTIVE
            """)
    List<DropdownOptions> findDropdownOptions();
}
