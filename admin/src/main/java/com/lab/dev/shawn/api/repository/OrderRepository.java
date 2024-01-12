package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Booking, Long> {
}
