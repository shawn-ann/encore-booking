package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.entity.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {


    @Query(value = """
            select new com.lab.dev.shawn.api.base.dto.DropdownOptions(a.id,a.name) 
            from Session a 
            where a.deleted=false 
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
