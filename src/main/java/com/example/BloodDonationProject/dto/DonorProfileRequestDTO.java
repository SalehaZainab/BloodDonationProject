package com.example.BloodDonationProject.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DonorProfileRequestDTO {
	@NotNull(message = "User ID is required")
	private Long userId;

	@NotBlank(message = "Availability is required")
	private String availability;

	private LocalDate lastDonationDate;
	private String healthConditions;

	public DonorProfileRequestDTO() {
	}

	public DonorProfileRequestDTO(Long userId, String availability) {
		this.userId = userId;
		this.availability = availability;
	}

	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }

	public String getAvailability() { return availability; }
	public void setAvailability(String availability) { this.availability = availability; }

	public LocalDate getLastDonationDate() { return lastDonationDate; }
	public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }

	public String getHealthConditions() { return healthConditions; }
	public void setHealthConditions(String healthConditions) { this.healthConditions = healthConditions; }
}
