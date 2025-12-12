package com.example.BloodDonationProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BloodDonationProject.entity.DonorProfile;

@Repository
public interface DonorRepository extends JpaRepository<DonorProfile, Long> {

	// Find donor by userId
	Optional<DonorProfile> findByUserId(Long userId);

	// Check if donor exists for a user
	boolean existsByUserId(Long userId);

	// Find all donors by availability status
	List<DonorProfile> findByAvailability(String availability);
}