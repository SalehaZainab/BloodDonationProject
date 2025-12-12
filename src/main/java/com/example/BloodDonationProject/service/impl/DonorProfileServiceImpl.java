package com.example.BloodDonationProject.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BloodDonationProject.dto.DonorProfileRequestDTO;
import com.example.BloodDonationProject.dto.DonorProfileResponseDTO;
import com.example.BloodDonationProject.entity.DonorProfile;
import com.example.BloodDonationProject.exception.AlreadyExistsException;
import com.example.BloodDonationProject.exception.BadRequestException;
import com.example.BloodDonationProject.exception.ResourceNotFoundException;
import com.example.BloodDonationProject.repository.DonorRepository;
import com.example.BloodDonationProject.service.DonorProfileService;
import com.example.BloodDonationProject.util.Availability;

@Service
@Transactional
public class DonorProfileServiceImpl implements DonorProfileService {

	@Autowired
	private DonorRepository donorRepository;

	@Override
	public DonorProfileResponseDTO createDonorProfile(DonorProfileRequestDTO dto) {
		// Validate: Check if donor already exists for this user
		if (donorRepository.existsByUserId(dto.getUserId())) {
			throw new AlreadyExistsException("Donor profile already exists for user ID: " + dto.getUserId());
		}

		// Validate: Check availability is valid
		try {
			Availability.fromString(dto.getAvailability());
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Invalid availability value: " + dto.getAvailability());
		}

		// Create new donor profile entity
		DonorProfile donor = new DonorProfile();
		donor.setUserId(dto.getUserId());
		donor.setAvailability(dto.getAvailability());
		donor.setLastDonationDate(dto.getLastDonationDate());
		donor.setHealthConditions(dto.getHealthConditions());

		// Save to database
		try {
			DonorProfile saved = donorRepository.save(donor);
			return toDto(saved);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create donor profile: " + e.getMessage(), e);
		}
	}

	@Override
	public DonorProfileResponseDTO updateDonorProfile(Long donorId, DonorProfileRequestDTO dto) {
		// Check if donor exists
		DonorProfile donor = donorRepository.findById(donorId)
			.orElseThrow(() -> new ResourceNotFoundException("Donor not found with ID: " + donorId));

		// Validate: Prevent changing userId for existing donor
		if (!donor.getUserId().equals(dto.getUserId())) {
			throw new BadRequestException("Cannot change userId for existing donor profile");
		}

		// Validate: Check availability is valid
		try {
			Availability.fromString(dto.getAvailability());
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Invalid availability value: " + dto.getAvailability());
		}

		// Update fields
		donor.setAvailability(dto.getAvailability());
		if (dto.getLastDonationDate() != null) {
			donor.setLastDonationDate(dto.getLastDonationDate());
		}
		if (dto.getHealthConditions() != null) {
			donor.setHealthConditions(dto.getHealthConditions());
		}

		// Save and return
		try {
			DonorProfile updated = donorRepository.save(donor);
			return toDto(updated);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update donor profile: " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public DonorProfileResponseDTO getDonorProfile(Long donorId) {
		DonorProfile donor = donorRepository.findById(donorId)
			.orElseThrow(() -> new ResourceNotFoundException("Donor not found with ID: " + donorId));
		return toDto(donor);
	}

	@Override
	@Transactional(readOnly = true)
	public DonorProfileResponseDTO getByUserId(Long userId) {
		DonorProfile donor = donorRepository.findByUserId(userId)
			.orElseThrow(() -> new ResourceNotFoundException("Donor not found for user ID: " + userId));
		return toDto(donor);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DonorProfileResponseDTO> getAllDonors() {
		return donorRepository.findAll()
			.stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DonorProfileResponseDTO> getAvailableDonors() {
		return donorRepository.findByAvailability(Availability.AVAILABLE.getValue())
			.stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public void deleteDonorProfile(Long donorId) {
		if (!donorRepository.existsById(donorId)) {
			throw new ResourceNotFoundException("Donor not found with ID: " + donorId);
		}
		try {
			donorRepository.deleteById(donorId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete donor profile: " + e.getMessage(), e);
		}
	}

	// Helper method to convert entity to DTO
	private DonorProfileResponseDTO toDto(DonorProfile donor) {
		DonorProfileResponseDTO dto = new DonorProfileResponseDTO();
		dto.setId(donor.getId() != null ? donor.getId().toString() : null);
		dto.setUserId(donor.getUserId());
		dto.setAvailability(donor.getAvailability());
		dto.setLastDonationDate(donor.getLastDonationDate());
		dto.setHealthConditions(donor.getHealthConditions());
		dto.setCreatedAt(donor.getCreatedAt());
		dto.setUpdatedAt(donor.getUpdatedAt());
		return dto;
	}
}