package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByAccountId(String accountId);
}
