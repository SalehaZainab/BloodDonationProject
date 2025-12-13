package com.example.BloodDonationProject.service;

import java.util.List;

import com.example.BloodDonationProject.dto.DonationInterestRequestDTO;
import com.example.BloodDonationProject.dto.DonationInterestResponseDTO;
import com.example.BloodDonationProject.util.InterestType;

/**
 * Service interface for donation interest operations
 * Defines business logic for managing donor interest in blood requests
 */
public interface DonationInterestService {

	/**
	 * Donor expresses interest in a blood request OR Requester shows interest in
	 * donor
	 */
	DonationInterestResponseDTO expressInterest(DonationInterestRequestDTO requestDTO);

	/**
	 * Blood request creator or donor approves an interest
	 */
	DonationInterestResponseDTO approveInterest(String interestId);

	/**
	 * Blood request creator or donor declines an interest
	 */
	DonationInterestResponseDTO declineInterest(String interestId);

	/**
	 * Get all donors interested in a specific blood request
	 */
	List<DonationInterestResponseDTO> getInterestsForRequest(String requestId);

	/**
	 * Get all blood requests a donor has expressed interest in
	 */
	List<DonationInterestResponseDTO> getInterestsForDonor(String donorId);

	/**
	 * Get interests by type (DONOR_TO_REQUEST or REQUESTER_TO_DONOR)
	 */
	List<DonationInterestResponseDTO> getInterestsByType(InterestType interestType);

	/**
	 * Get interests for a request filtered by type
	 */
	List<DonationInterestResponseDTO> getInterestsForRequestByType(String requestId, InterestType interestType);

	/**
	 * Get interests from a donor filtered by type
	 */
	List<DonationInterestResponseDTO> getInterestsForDonorByType(String donorId, InterestType interestType);

	/**
	 * Delete an interest record
	 */
	void deleteInterest(String interestId);

	/**
	 * Get a specific interest by ID
	 */
	DonationInterestResponseDTO getInterestById(String interestId);
}
