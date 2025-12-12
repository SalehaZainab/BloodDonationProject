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

import com.example.BloodDonationProject.dto.ApiResponse;
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
	public ResponseEntity<ApiResponse<DonorProfileResponseDTO>> createDonor(@Valid @RequestBody DonorProfileRequestDTO dto) {
		DonorProfileResponseDTO response = service.createDonorProfile(dto);
		ApiResponse<DonorProfileResponseDTO> body = ApiResponse.success("Donor profile created successfully", response);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	// Get donor profile by donorId
	@GetMapping("/{donorId}")
	public ResponseEntity<ApiResponse<DonorProfileResponseDTO>> getDonorById(@PathVariable Long donorId) {
		DonorProfileResponseDTO response = service.getDonorProfile(donorId);
		ApiResponse<DonorProfileResponseDTO> body = ApiResponse.success("Donor profile fetched successfully", response);
		return ResponseEntity.ok(body);
	}

	// Get donor profile by userId
	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<DonorProfileResponseDTO>> getDonorByUserId(@PathVariable Long userId) {
		DonorProfileResponseDTO response = service.getByUserId(userId);
		ApiResponse<DonorProfileResponseDTO> body = ApiResponse.success("Donor profile fetched successfully", response);
		return ResponseEntity.ok(body);
	}

	// Get all donors
	@GetMapping
	public ResponseEntity<ApiResponse<List<DonorProfileResponseDTO>>> getAllDonors() {
		List<DonorProfileResponseDTO> response = service.getAllDonors();
		ApiResponse<List<DonorProfileResponseDTO>> body = ApiResponse.success("Donor profiles fetched successfully", response);
		return ResponseEntity.ok(body);
	}

	// Get all available donors
	@GetMapping("/available")
	public ResponseEntity<ApiResponse<List<DonorProfileResponseDTO>>> getAvailableDonors() {
		List<DonorProfileResponseDTO> response = service.getAvailableDonors();
		ApiResponse<List<DonorProfileResponseDTO>> body = ApiResponse.success("Available donors fetched successfully", response);
		return ResponseEntity.ok(body);
	}

	// Update donor profile
	@PutMapping("/{donorId}")
	public ResponseEntity<ApiResponse<DonorProfileResponseDTO>> updateDonor(
			@PathVariable Long donorId,
			@Valid @RequestBody DonorProfileRequestDTO dto) {
		DonorProfileResponseDTO response = service.updateDonorProfile(donorId, dto);
		ApiResponse<DonorProfileResponseDTO> body = ApiResponse.success("Donor profile updated successfully", response);
		return ResponseEntity.ok(body);
	}

	// Delete donor profile
	@DeleteMapping("/{donorId}")
	public ResponseEntity<ApiResponse<Void>> deleteDonor(@PathVariable Long donorId) {
		service.deleteDonorProfile(donorId);
		ApiResponse<Void> body = ApiResponse.success("Donor profile deleted successfully");
		return ResponseEntity.ok(body);
	}
}