package com.example.BloodDonationProject.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "donor_profile")
public class DonorProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "donor_id")
	private Long donorId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "availability", nullable = false, length = 20)
	private String availability;

	@Column(name = "last_donation_date")
	private LocalDate lastDonationDate;

	@Column(name = "health_conditions", columnDefinition = "text")
	private String healthConditions;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public DonorProfile() {
	}

	public DonorProfile(Long userId, String availability) {
		this.userId = userId;
		this.availability = availability;
	}

	public Long getDonorId() {
		return donorId;
	}

	public void setDonorId(Long donorId) {
		this.donorId = donorId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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
