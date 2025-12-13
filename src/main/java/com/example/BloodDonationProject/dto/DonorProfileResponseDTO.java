package com.example.BloodDonationProject.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DonorProfileResponseDTO {
	private String id;
	private String userId;
	private String availability;
	private LocalDate lastDonationDate;
	private String healthConditions;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public DonorProfileResponseDTO() {
	}

	public DonorProfileResponseDTO(String id, String userId, String availability) {
		this.id = id;
		this.userId = userId;
		this.availability = availability;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public LocalDate getLastDonationDate() {
		return lastDonationDate;
	}

	public void setLastDonationDate(LocalDate lastDonationDate) {
		this.lastDonationDate = lastDonationDate;
	}

	public String getHealthConditions() {
		return healthConditions;
	}

	public void setHealthConditions(String healthConditions) {
		this.healthConditions = healthConditions;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
