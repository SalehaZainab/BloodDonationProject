package com.example.BloodDonationProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BloodDonationProject.dto.DonorProfileRequestDTO;
import com.example.BloodDonationProject.dto.DonorProfileResponseDTO;
import com.example.BloodDonationProject.service.DonorProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*")
public class DonorController {

	@Autowired
	private DonorProfileService service;

	// Create a new donor profile
	@PostMapping
	public ResponseEntity<DonorProfileResponseDTO> createDonor(@Valid @RequestBody DonorProfileRequestDTO dto) {
		DonorProfileResponseDTO response = service.createDonorProfile(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Get donor profile by donorId
	@GetMapping("/{donorId}")
	public ResponseEntity<DonorProfileResponseDTO> getDonorById(@PathVariable Long donorId) {
		DonorProfileResponseDTO response = service.getDonorProfile(donorId);
		return ResponseEntity.ok(response);
	}

	// Get donor profile by userId
	@GetMapping("/user/{userId}")
	public ResponseEntity<DonorProfileResponseDTO> getDonorByUserId(@PathVariable Long userId) {
		DonorProfileResponseDTO response = service.getByUserId(userId);
		return ResponseEntity.ok(response);
	}

	// Get all donors
	@GetMapping
	public ResponseEntity<List<DonorProfileResponseDTO>> getAllDonors() {
		List<DonorProfileResponseDTO> response = service.getAllDonors();
		return ResponseEntity.ok(response);
	}

	// Get all available donors
	@GetMapping("/available")
	public ResponseEntity<List<DonorProfileResponseDTO>> getAvailableDonors() {
		List<DonorProfileResponseDTO> response = service.getAvailableDonors();
		return ResponseEntity.ok(response);
	}

	// Update donor profile
	@PutMapping("/{donorId}")
	public ResponseEntity<DonorProfileResponseDTO> updateDonor(
			@PathVariable Long donorId,
			@Valid @RequestBody DonorProfileRequestDTO dto) {
		DonorProfileResponseDTO response = service.updateDonorProfile(donorId, dto);
		return ResponseEntity.ok(response);
	}

	// Delete donor profile
	@DeleteMapping("/{donorId}")
	public ResponseEntity<Void> deleteDonor(@PathVariable Long donorId) {
		service.deleteDonorProfile(donorId);
		return ResponseEntity.noContent().build();
	}
}