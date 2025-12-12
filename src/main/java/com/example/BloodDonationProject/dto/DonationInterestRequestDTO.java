package com.example.BloodDonationProject.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating/updating donation interest
 * Used when donor expresses interest or requester sends interest to donor
 */
public class DonationInterestRequestDTO {
	
	@NotNull(message = "Donor ID is required")
	private Long donorId;
	
	@NotNull(message = "Request ID is required")
	private Long requestId;
	
	// Constructor
	public DonationInterestRequestDTO() {
	}
	
	public DonationInterestRequestDTO(Long donorId, Long requestId) {
		this.donorId = donorId;
		this.requestId = requestId;
	}
	
	// Getters and Setters
	public Long getDonorId() {
		return donorId;
	}
	
	public void setDonorId(Long donorId) {
		this.donorId = donorId;
	}
	
	public Long getRequestId() {
		return requestId;
	}
	
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
}
