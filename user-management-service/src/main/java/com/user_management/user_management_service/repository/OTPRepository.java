package com.user_management.user_management_service.repository;

import com.user_management.user_management_service.model.OTPRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTPRecord, Long> {

    Optional<OTPRecord> findByOtpAndIsValid(String otp, boolean isValid);

    Optional<OTPRecord> findByOtp(String otp);

    void deleteAllByExpiryTimeBefore(LocalDateTime now);

    Optional<OTPRecord> findByOtpAndIsValidTrue(String otp);
}
