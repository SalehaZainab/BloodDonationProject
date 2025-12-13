package com.example.BloodDonationProject.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.DonorProfile;

@Repository
public interface DonorRepository extends JpaRepository<DonorProfile, UUID> {

	// Find donor by userId
	Optional<DonorProfile> findByUserId(String userId);

	// Check if donor exists for a user
	boolean existsByUserId(String userId);

	// Find all donors by availability status
	List<DonorProfile> findByAvailability(String availability);

	// Find donors by blood group and city for matching
	List<DonorProfile> findByBloodGroupAndCity(BloodGroup bloodGroup, String city);

	// Find available donors by blood group and city
	List<DonorProfile> findByBloodGroupAndCityAndAvailability(BloodGroup bloodGroup, String city, String availability);
}