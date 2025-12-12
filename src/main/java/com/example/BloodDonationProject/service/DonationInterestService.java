package com.example.BloodDonationProject.service;

import java.util.List;

import com.example.BloodDonationProject.dto.DonationInterestRequestDTO;
import com.example.BloodDonationProject.dto.DonationInterestResponseDTO;

/**
 * Service interface for donation interest operations
 * Defines business logic for managing donor interest in blood requests
 */
public interface DonationInterestService {
	
	/**
	 * Donor expresses interest in a blood request
	 */
	DonationInterestResponseDTO expressInterest(DonationInterestRequestDTO requestDTO);
	
	/**
	 * Blood request creator approves a donor's interest
	 */
	DonationInterestResponseDTO approveInterest(Long interestId);
	
	/**
	 * Blood request creator declines a donor's interest
	 */
	DonationInterestResponseDTO declineInterest(Long interestId);
	
	/**
	 * Get all donors interested in a specific blood request
	 */
	List<DonationInterestResponseDTO> getInterestsForRequest(Long requestId);
	
	/**
	 * Get all blood requests a donor has expressed interest in
	 */
	List<DonationInterestResponseDTO> getInterestsForDonor(Long donorId);
	
	/**
	 * Delete an interest record
	 */
	void deleteInterest(Long interestId);
	
	/**
	 * Get a specific interest by ID
	 */
	DonationInterestResponseDTO getInterestById(Long interestId);
}
