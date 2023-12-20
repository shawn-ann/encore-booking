package com.lab.dev.shawn.admin.repository;

import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
