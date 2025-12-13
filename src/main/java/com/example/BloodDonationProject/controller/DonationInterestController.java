package com.example.BloodDonationProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BloodDonationProject.dto.ApiResponse;
import com.example.BloodDonationProject.dto.DonationInterestRequestDTO;
import com.example.BloodDonationProject.dto.DonationInterestResponseDTO;
import com.example.BloodDonationProject.security.RequireAuth;
import com.example.BloodDonationProject.service.DonationInterestService;
import com.example.BloodDonationProject.util.InterestType;

import jakarta.validation.Valid;

/**
 * Controller for donation interest endpoints
 * Handles REST API for managing donor interest in blood requests
 */
@RestController
@RequestMapping("/api/interests")
public class DonationInterestController {

	@Autowired
	private DonationInterestService donationInterestService;

	/**
	 * POST /api/interests
	 * Donor expresses interest in a blood request
	 */
	@RequireAuth
	@PostMapping
	public ResponseEntity<ApiResponse<DonationInterestResponseDTO>> expressInterest(
			@Valid @RequestBody DonationInterestRequestDTO requestDTO) {
		DonationInterestResponseDTO response = donationInterestService.expressInterest(requestDTO);
		ApiResponse<DonationInterestResponseDTO> body = ApiResponse.success("Interest expressed successfully",
				response);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	/**
	 * PUT /api/interests/{interestId}/approve
	 * Blood request creator approves a donor's interest
	 */
	@RequireAuth
	@PutMapping("/{interestId}/approve")
	public ResponseEntity<ApiResponse<DonationInterestResponseDTO>> approveInterest(
			@PathVariable String interestId) {
		DonationInterestResponseDTO response = donationInterestService.approveInterest(interestId);
		ApiResponse<DonationInterestResponseDTO> body = ApiResponse.success("Interest approved successfully", response);
		return ResponseEntity.ok(body);
	}

	/**
	 * PUT /api/interests/{interestId}/decline
	 * Blood request creator declines a donor's interest
	 */
	@RequireAuth
	@PutMapping("/{interestId}/decline")
	public ResponseEntity<ApiResponse<DonationInterestResponseDTO>> declineInterest(
			@PathVariable String interestId) {
		DonationInterestResponseDTO response = donationInterestService.declineInterest(interestId);
		ApiResponse<DonationInterestResponseDTO> body = ApiResponse.success("Interest declined successfully", response);
		return ResponseEntity.ok(body);
	}

	/**
	 * GET /api/interests/request/{requestId}
	 * List all donors interested in a specific blood request
	 */
	@GetMapping("/request/{requestId}")
	public ResponseEntity<ApiResponse<List<DonationInterestResponseDTO>>> getInterestsForRequest(
			@PathVariable String requestId) {
		List<DonationInterestResponseDTO> interests = donationInterestService.getInterestsForRequest(requestId);
		ApiResponse<List<DonationInterestResponseDTO>> body = ApiResponse.success("Interests fetched successfully",
				interests);
		return ResponseEntity.ok(body);
	}

	/**
	 * GET /api/interests/donor/{donorId}
	 * List all blood requests a donor has expressed interest in
	 */
	@GetMapping("/donor/{donorId}")
	public ResponseEntity<ApiResponse<List<DonationInterestResponseDTO>>> getInterestsForDonor(
			@PathVariable String donorId) {
		List<DonationInterestResponseDTO> interests = donationInterestService.getInterestsForDonor(donorId);
		ApiResponse<List<DonationInterestResponseDTO>> body = ApiResponse.success("Interests fetched successfully",
				interests);
		return ResponseEntity.ok(body);
	}

	/**
	 * GET /api/interests/{interestId}
	 * Get a specific interest by ID
	 */
	@GetMapping("/{interestId}")
	public ResponseEntity<ApiResponse<DonationInterestResponseDTO>> getInterestById(
			@PathVariable String interestId) {
		DonationInterestResponseDTO interest = donationInterestService.getInterestById(interestId);
		ApiResponse<DonationInterestResponseDTO> body = ApiResponse.success("Interest fetched successfully", interest);
		return ResponseEntity.ok(body);
	}

	/**
	 * DELETE /api/interests/{interestId}
	 * Delete an interest record
	 */
	@RequireAuth
	@DeleteMapping("/{interestId}")
	public ResponseEntity<ApiResponse<Void>> deleteInterest(
			@PathVariable String interestId) {
		donationInterestService.deleteInterest(interestId);
		ApiResponse<Void> body = ApiResponse.success("Interest deleted successfully");
		return ResponseEntity.ok(body);
	}

	/**
	 * GET /api/interests/type/{interestType}
	 * Get all interests by type (DONOR_TO_REQUEST or REQUESTER_TO_DONOR)
	 */
	@GetMapping("/type/{interestType}")
	public ResponseEntity<ApiResponse<List<DonationInterestResponseDTO>>> getInterestsByType(
			@PathVariable InterestType interestType) {
		List<DonationInterestResponseDTO> interests = donationInterestService.getInterestsByType(interestType);
		ApiResponse<List<DonationInterestResponseDTO>> body = ApiResponse.success("Interests fetched successfully",
				interests);
		return ResponseEntity.ok(body);
	}

	/**
	 * GET /api/interests/request/{requestId}/type
	 * Get interests for a request filtered by type
	 */
	@GetMapping("/request/{requestId}/type")
	public ResponseEntity<ApiResponse<List<DonationInterestResponseDTO>>> getInterestsForRequestByType(
			@PathVariable String requestId,
			@RequestParam InterestType interestType) {
		List<DonationInterestResponseDTO> interests = donationInterestService.getInterestsForRequestByType(requestId,
				interestType);
		ApiResponse<List<DonationInterestResponseDTO>> body = ApiResponse.success("Interests fetched successfully",
				interests);
		return ResponseEntity.ok(body);
	}

	/**
	 * GET /api/interests/donor/{donorId}/type
	 * Get interests from a donor filtered by type
	 */
	@GetMapping("/donor/{donorId}/type")
	public ResponseEntity<ApiResponse<List<DonationInterestResponseDTO>>> getInterestsForDonorByType(
			@PathVariable String donorId,
			@RequestParam InterestType interestType) {
		List<DonationInterestResponseDTO> interests = donationInterestService.getInterestsForDonorByType(donorId,
				interestType);
		ApiResponse<List<DonationInterestResponseDTO>> body = ApiResponse.success("Interests fetched successfully",
				interests);
		return ResponseEntity.ok(body);
	}
}
