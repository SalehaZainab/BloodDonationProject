package com.example.BloodDonationProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BloodDonationProject.entity.DonationInterest;

/**
 * Repository for DonationInterest entity
 * Handles database operations for donation interest records
 */
@Repository
public interface DonationInterestRepository extends JpaRepository<DonationInterest, Long> {
	
	/**
	 * Find all interests for a specific blood request
	 * Shows all donors interested in one request
	 */
	List<DonationInterest> findByRequestId(Long requestId);
	
	/**
	 * Find all interests expressed by a specific donor
	 * Shows all requests a donor expressed interest in
	 */
	List<DonationInterest> findByDonorId(Long donorId);
	
	/**
	 * Find a specific interest between a donor and request
	 * Used to prevent duplicate interests
	 */
	Optional<DonationInterest> findByDonorIdAndRequestId(Long donorId, Long requestId);
}
