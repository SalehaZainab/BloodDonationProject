package com.example.BloodDonationProject.dto;

import java.time.LocalDate;

import com.example.BloodDonationProject.entity.BloodGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DonorProfileRequestDTO {
	@NotNull(message = "User ID is required")
	private String userId;

	@NotBlank(message = "Availability is required")
	private String availability;

	private LocalDate lastDonationDate;
	private String healthConditions;

	@NotNull(message = "Blood group is required")
	private BloodGroup bloodGroup;

	@NotBlank(message = "City is required")
	private String city;

	public DonorProfileRequestDTO() {
	}

	public DonorProfileRequestDTO(String userId, String availability) {
		this.userId = userId;
		this.availability = availability;
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

	public BloodGroup getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
