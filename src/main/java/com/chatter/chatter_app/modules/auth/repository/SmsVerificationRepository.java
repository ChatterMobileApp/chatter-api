package com.chatter.chatter_app.modules.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chatter.chatter_app.modules.auth.model.SmsVerification;

@Repository
public interface SmsVerificationRepository extends JpaRepository<SmsVerification, UUID> {
    
    Optional<SmsVerification> findByPhoneNumberAndCodeAndIsUsedFalse(String phoneNumber, String code);
    
    @Modifying
    @Query("DELETE FROM SmsVerification s WHERE s.phoneNumber = :phoneNumber")
    void deleteByPhoneNumber(String phoneNumber);
    
    @Query("SELECT COUNT(s) FROM SmsVerification s WHERE s.phoneNumber = :phoneNumber AND s.createdAt > :since")
    long countByPhoneNumberAndCreatedAtAfter(String phoneNumber, java.time.LocalDateTime since);
}
