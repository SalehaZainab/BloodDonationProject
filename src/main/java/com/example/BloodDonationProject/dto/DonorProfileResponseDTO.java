package com.example.BloodDonationProject.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DonorProfileResponseDTO {
	private Long donorId;
	private Long userId;
	private String availability;
	private LocalDate lastDonationDate;
	private String healthConditions;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public DonorProfileResponseDTO() {
	}

	public DonorProfileResponseDTO(Long donorId, Long userId, String availability) {
		this.donorId = donorId;
		this.userId = userId;
		this.availability = availability;
	}

	public Long getDonorId() { return donorId; }
	public void setDonorId(Long donorId) { this.donorId = donorId; }

	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }

	public String getAvailability() { return availability; }
	public void setAvailability(String availability) { this.availability = availability; }

	public LocalDate getLastDonationDate() { return lastDonationDate; }
	public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }

	public String getHealthConditions() { return healthConditions; }
	public void setHealthConditions(String healthConditions) { this.healthConditions = healthConditions; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
