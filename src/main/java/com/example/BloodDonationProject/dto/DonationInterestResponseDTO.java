package com.example.BloodDonationProject.dto;

import java.time.LocalDateTime;

/**
 * DTO for returning donation interest information
 * Sent back to frontend with complete interest details
 */
public class DonationInterestResponseDTO {
	
	private String id;
	private String donorId;
	private String requestId;
	private String status;
	private LocalDateTime createdAt;
	
	// Constructor
	public DonationInterestResponseDTO() {
	}
	
	public DonationInterestResponseDTO(String id, String donorId, String requestId, 
	                                    String status, LocalDateTime createdAt) {
		this.id = id;
		this.donorId = donorId;
		this.requestId = requestId;
		this.status = status;
		this.createdAt = createdAt;
	}
	
	// Getters and Setters
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
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
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
