package com.example.BloodDonationProject.repository;

import com.example.BloodDonationProject.entity.DonationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationHistoryRepository extends JpaRepository<DonationHistory, String> {

    // Find by ID with soft delete check
    Optional<DonationHistory> findByIdAndDeletedAtIsNull(String id);

    // Find all donations by donor
    List<DonationHistory> findByDonorIdAndDeletedAtIsNullOrderByDonationDateDesc(String donorId);

    // Find all donations by recipient
    List<DonationHistory> findByRecipientIdAndDeletedAtIsNullOrderByDonationDateDesc(String recipientId);

    // Find donations by blood request ID
    List<DonationHistory> findByBloodRequestIdAndDeletedAtIsNull(String bloodRequestId);

    // Find all non-deleted donations
    List<DonationHistory> findByDeletedAtIsNullOrderByDonationDateDesc();

    // Find donations by city
    List<DonationHistory> findByCityAndDeletedAtIsNullOrderByDonationDateDesc(String city);

    // Count total donations by donor
    @Query("SELECT COUNT(d) FROM DonationHistory d WHERE d.donorId = :donorId AND d.deletedAt IS NULL")
    long countByDonorId(@Param("donorId") String donorId);

    // Find donations within date range
    @Query("SELECT d FROM DonationHistory d WHERE d.donationDate BETWEEN :startDate AND :endDate AND d.deletedAt IS NULL ORDER BY d.donationDate DESC")
    List<DonationHistory> findByDonationDateBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find recent donations (last 30 days) by donor
    @Query("SELECT d FROM DonationHistory d WHERE d.donorId = :donorId AND d.donationDate >= :sinceDate AND d.deletedAt IS NULL ORDER BY d.donationDate DESC")
    List<DonationHistory> findRecentDonationsByDonor(@Param("donorId") String donorId,
            @Param("sinceDate") LocalDateTime sinceDate);
}
