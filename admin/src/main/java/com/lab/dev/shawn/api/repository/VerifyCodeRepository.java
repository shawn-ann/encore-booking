package com.lab.dev.shawn.api.repository;

import com.lab.dev.shawn.api.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {


    @Query("""
            Select a
            from VerifyCode a 
            where a.verified=false 
            and a.isLogin= :isLogin
            and a.mobile= :mobile
            and a.createDate > :createDate
            """)
    List<VerifyCode> findByMobileAndIsLoginAndCreateDateGreaterThan(boolean isLogin, String mobile, LocalDateTime createDate);

    @Query("""
            Select a
            from VerifyCode a 
            where a.verified=false 
            and a.isLogin= :isLogin 
            and a.mobile= :mobile
            and a.expiredDate >= CURRENT_TIMESTAMP
            order by a.createDate desc
            limit 1
            """)
    VerifyCode findNotExpiredByMobileAndIsLogin(boolean isLogin, String mobile);
}
