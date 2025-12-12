package com.example.BloodDonationProject.repository;

import com.example.BloodDonationProject.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {

    Optional<Otp> findByEmailAndOtpCodeAndVerifiedFalse(String email, String otpCode);

    Optional<Otp> findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(String email);

    void deleteByEmail(String email);

    void deleteByExpiryTimeBefore(LocalDateTime dateTime);
}
