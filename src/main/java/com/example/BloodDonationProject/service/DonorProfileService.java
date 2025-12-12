package com.example.BloodDonationProject.service;

import java.util.List;

import com.example.BloodDonationProject.dto.DonorProfileRequestDTO;
import com.example.BloodDonationProject.dto.DonorProfileResponseDTO;

public interface DonorProfileService {

	// Create a new donor profile
	DonorProfileResponseDTO createDonorProfile(DonorProfileRequestDTO dto);

	// Update an existing donor profile
	DonorProfileResponseDTO updateDonorProfile(Long donorId, DonorProfileRequestDTO dto);

	// Get donor profile by donorId
	DonorProfileResponseDTO getDonorProfile(Long donorId);

	// Get donor profile by userId
	DonorProfileResponseDTO getByUserId(Long userId);

	// Get all donor profiles
	List<DonorProfileResponseDTO> getAllDonors();

	// Get all available donors
	List<DonorProfileResponseDTO> getAvailableDonors();

	// Delete donor profile
	void deleteDonorProfile(Long donorId);
}