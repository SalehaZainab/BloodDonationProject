package com.example.BloodDonationProject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BloodDonationProject.dto.DonationInterestRequestDTO;
import com.example.BloodDonationProject.dto.DonationInterestResponseDTO;
import com.example.BloodDonationProject.entity.BloodRequest;
import com.example.BloodDonationProject.entity.DonationInterest;
import com.example.BloodDonationProject.entity.DonorProfile;
import com.example.BloodDonationProject.exception.BadRequestException;
import com.example.BloodDonationProject.exception.ResourceNotFoundException;
import com.example.BloodDonationProject.repository.BloodRequestRepository;
import com.example.BloodDonationProject.repository.DonationInterestRepository;
import com.example.BloodDonationProject.repository.DonorRepository;
import com.example.BloodDonationProject.service.DonationInterestService;

/**
 * Service implementation for donation interest operations
 * Handles business logic for managing donor interest in blood requests
 */
@Service
@Transactional
public class DonationInterestServiceImpl implements DonationInterestService {
	
	@Autowired
	private DonationInterestRepository donationInterestRepository;
	
	@Autowired
	private DonorRepository donorRepository;
	
	@Autowired
	private BloodRequestRepository bloodRequestRepository;
	
	/**
	 * Donor expresses interest in a blood request
	 * Validates donor and request existence, prevents duplicate interests
	 */
	@Override
	public DonationInterestResponseDTO expressInterest(DonationInterestRequestDTO requestDTO) {
		Long donorId = requestDTO.getDonorId();
		Long requestId = requestDTO.getRequestId();
		
		// Validate donor exists
		DonorProfile donor = donorRepository.findById(donorId)
			.orElseThrow(() -> new ResourceNotFoundException("Donor not found with ID: " + donorId));
		
		// Validate request exists
		BloodRequest request = bloodRequestRepository.findById(requestId)
			.orElseThrow(() -> new ResourceNotFoundException("Blood request not found with ID: " + requestId));
		
		// Check if interest already exists (prevent duplicates)
		var existingInterest = donationInterestRepository.findByDonorIdAndRequestId(donorId, requestId);
		if (existingInterest.isPresent()) {
			throw new BadRequestException("Donor has already expressed interest in this request");
		}
		
		// Create new interest with status = PENDING
		DonationInterest interest = new DonationInterest(donor, request);
		interest.setStatus("PENDING");
		
		DonationInterest savedInterest = donationInterestRepository.save(interest);
		return mapToResponseDTO(savedInterest);
	}
	
	/**
	 * Approve a donor's interest (Blood request creator action)
	 */
	@Override
	public DonationInterestResponseDTO approveInterest(Long interestId) {
		DonationInterest interest = donationInterestRepository.findById(interestId)
			.orElseThrow(() -> new ResourceNotFoundException("Interest not found with ID: " + interestId));
		
		if (!"PENDING".equals(interest.getStatus())) {
			throw new BadRequestException("Only PENDING interests can be approved. Current status: " + interest.getStatus());
		}
		
		interest.setStatus("APPROVED");
		DonationInterest updatedInterest = donationInterestRepository.save(interest);
		return mapToResponseDTO(updatedInterest);
	}
	
	/**
	 * Decline a donor's interest (Blood request creator action)
	 */
	@Override
	public DonationInterestResponseDTO declineInterest(Long interestId) {
		DonationInterest interest = donationInterestRepository.findById(interestId)
			.orElseThrow(() -> new ResourceNotFoundException("Interest not found with ID: " + interestId));
		
		if (!"PENDING".equals(interest.getStatus())) {
			throw new BadRequestException("Only PENDING interests can be declined. Current status: " + interest.getStatus());
		}
		
		interest.setStatus("DECLINED");
		DonationInterest updatedInterest = donationInterestRepository.save(interest);
		return mapToResponseDTO(updatedInterest);
	}
	
	/**
	 * Get all donors interested in a specific blood request
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DonationInterestResponseDTO> getInterestsForRequest(Long requestId) {
		// Validate request exists
		bloodRequestRepository.findById(requestId)
			.orElseThrow(() -> new ResourceNotFoundException("Blood request not found with ID: " + requestId));
		
		List<DonationInterest> interests = donationInterestRepository.findByRequestId(requestId);
		return interests.stream()
			.map(this::mapToResponseDTO)
			.collect(Collectors.toList());
	}
	
	/**
	 * Get all blood requests a donor has expressed interest in
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DonationInterestResponseDTO> getInterestsForDonor(Long donorId) {
		// Validate donor exists
		donorRepository.findById(donorId)
			.orElseThrow(() -> new ResourceNotFoundException("Donor not found with ID: " + donorId));
		
		List<DonationInterest> interests = donationInterestRepository.findByDonorId(donorId);
		return interests.stream()
			.map(this::mapToResponseDTO)
			.collect(Collectors.toList());
	}
	
	/**
	 * Delete an interest record
	 */
	@Override
	public void deleteInterest(Long interestId) {
		DonationInterest interest = donationInterestRepository.findById(interestId)
			.orElseThrow(() -> new ResourceNotFoundException("Interest not found with ID: " + interestId));
		
		donationInterestRepository.delete(interest);
	}
	
	/**
	 * Get a specific interest by ID
	 */
	@Override
	@Transactional(readOnly = true)
	public DonationInterestResponseDTO getInterestById(Long interestId) {
		DonationInterest interest = donationInterestRepository.findById(interestId)
			.orElseThrow(() -> new ResourceNotFoundException("Interest not found with ID: " + interestId));
		
		return mapToResponseDTO(interest);
	}
	
	/**
	 * Helper method to map DonationInterest entity to response DTO
	 */
	private DonationInterestResponseDTO mapToResponseDTO(DonationInterest interest) {
		return new DonationInterestResponseDTO(
			interest.getId() != null ? interest.getId().toString() : null,
			interest.getDonor().getId() != null ? interest.getDonor().getId().toString() : null,
			interest.getRequest().getId() != null ? interest.getRequest().getId().toString() : null,
			interest.getStatus(),
			interest.getCreatedAt()
		);
	}
}
