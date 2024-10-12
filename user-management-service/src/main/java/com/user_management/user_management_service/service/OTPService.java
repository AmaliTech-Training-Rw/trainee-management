package com.user_management.user_management_service.service;

import com.user_management.user_management_service.model.OTPRecord;
import com.user_management.user_management_service.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OTPService {

    @Autowired
    private OTPRepository otpRepository;

    private static final Logger logger = LoggerFactory.getLogger(OTPService.class);

    public String generateOTP(Integer userId) {
        // Generate a random OTP (6 digits)
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        // Set an expiry time (e.g., 10 minutes from now)
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);
        storeOTP(userId, otp, expiryTime);

        logger.info("Generated OTP: {} for userId: {}", otp, userId);
        return otp;
    }

    public void storeOTP(Integer userId, String otp, LocalDateTime expiryTime) {
        // Create a new OTP record to store in the database
        OTPRecord otpRecord = new OTPRecord();
        otpRecord.setUserId(userId);
        otpRecord.setOtp(otp);
        otpRecord.setExpiryTime(expiryTime);
        otpRecord.setValid(true);
        otpRepository.save(otpRecord);

        logger.info("Stored OTP: {} for userId: {} with expiryTime: {}", otp, userId, expiryTime);
    }

    public void removeOTP(String otp) {
        Optional<OTPRecord> otpRecordOptional = otpRepository.findByOtp(otp);
        if (otpRecordOptional.isPresent()) {
            OTPRecord otpRecord = otpRecordOptional.get();
            otpRecord.setValid(false);
            otpRepository.save(otpRecord);
            logger.info("Invalidated OTP: {}", otp);
        }
    }

    public Integer getUserIdByOTP(String otp) {
        Optional<OTPRecord> otpRecordOptional = otpRepository.findByOtpAndIsValidTrue(otp);

        if (otpRecordOptional.isPresent()) {
            OTPRecord otpRecord = otpRecordOptional.get();
            if (LocalDateTime.now().isAfter(otpRecord.getExpiryTime())) {
                otpRecord.setValid(false);
                otpRepository.save(otpRecord);
                logger.warn("OTP: {} expired for userId: {}", otp, otpRecord.getUserId());
                return null;
            }
            logger.info("Retrieved OTP: {} for userId: {}", otp, otpRecord.getUserId());
            return otpRecord.getUserId();
        }

        logger.warn("OTP: {} not found or invalid", otp);
        return null;
    }

    public void invalidateOTP(String otp) {
        Optional<OTPRecord> otpRecordOptional = otpRepository.findByOtp(otp);

        if (otpRecordOptional.isPresent()) {
            otpRepository.delete(otpRecordOptional.get());
            logger.info("OTP: {} deleted", otp);
        } else {
            logger.warn("Attempted to delete non-existent OTP: {}", otp);
        }
    }
}
