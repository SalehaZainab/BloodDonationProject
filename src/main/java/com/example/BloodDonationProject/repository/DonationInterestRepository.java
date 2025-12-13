package com.example.BloodDonationProject.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BloodDonationProject.entity.DonationInterest;
import com.example.BloodDonationProject.util.InterestType;

/**
 * Repository for DonationInterest entity
 * Handles database operations for donation interest records
 */
@Repository
public interface DonationInterestRepository extends JpaRepository<DonationInterest, UUID> {

	/**
	 * Find all interests for a specific blood request
	 * Shows all donors interested in one request
	 */
	List<DonationInterest> findByRequestId(UUID requestId);

	/**
	 * Find all interests expressed by a specific donor
	 * Shows all requests a donor expressed interest in
	 */
	List<DonationInterest> findByDonorId(UUID donorId);

	/**
	 * Find a specific interest between a donor and request
	 * Used to prevent duplicate interests
	 */
	Optional<DonationInterest> findByDonorIdAndRequestId(UUID donorId, UUID requestId);

	/**
	 * Find interests by type (DONOR_TO_REQUEST or REQUESTER_TO_DONOR)
	 */
	List<DonationInterest> findByInterestType(InterestType interestType);

	/**
	 * Find interests for a request by type
	 */
	List<DonationInterest> findByRequestIdAndInterestType(UUID requestId, InterestType interestType);

	/**
	 * Find interests from a donor by type
	 */
	List<DonationInterest> findByDonorIdAndInterestType(UUID donorId, InterestType interestType);
}
