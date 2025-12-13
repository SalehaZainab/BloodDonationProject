package com.example.BloodDonationProject.dto;

import com.example.BloodDonationProject.util.InterestType;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating/updating donation interest
 * Used when donor expresses interest or requester sends interest to donor
 */
public class DonationInterestRequestDTO {

	@NotNull(message = "Donor ID is required")
	private String donorId;

	@NotNull(message = "Request ID is required")
	private String requestId;

	@NotNull(message = "Interest type is required")
	private InterestType interestType; // DONOR_TO_REQUEST or REQUESTER_TO_DONOR

	// Constructor
	public DonationInterestRequestDTO() {
	}

	public DonationInterestRequestDTO(String donorId, String requestId, InterestType interestType) {
		this.donorId = donorId;
		this.requestId = requestId;
		this.interestType = interestType;
	}

	// Getters and Setters
	public String getDonorId() {
		return donorId;
	}

	public void setDonorId(String donorId) {
		this.donorId = donorId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public InterestType getInterestType() {
		return interestType;
	}

	public void setInterestType(InterestType interestType) {
		this.interestType = interestType;
	}
}
