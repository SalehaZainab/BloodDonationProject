package com.example.BloodDonationProject.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * DonationInterest entity - Link between DonorProfile and BloodRequest
 * Represents interest of a donor in a blood request
 * 
 * One record = One donor + one blood request + their status
 */
@Entity
@Table(name = "donation_interest")
public class DonationInterest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "donor_id", nullable = false)
	private DonorProfile donor;
	
	@ManyToOne
	@JoinColumn(name = "request_id", nullable = false)
	private BloodRequest request;
	
	@Column(name = "status", nullable = false, length = 20)
	private String status = "PENDING";  // Default to PENDING
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	// Constructors
	public DonationInterest() {
	}
	
	public DonationInterest(DonorProfile donor, BloodRequest request) {
		this.donor = donor;
		this.request = request;
		this.status = "PENDING";
	}
	
	// Getters and Setters
public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public DonorProfile getDonor() {
		return donor;
	}
	
	public void setDonor(DonorProfile donor) {
		this.donor = donor;
	}
	
	public BloodRequest getRequest() {
		return request;
	}
	
	public void setRequest(BloodRequest request) {
		this.request = request;
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
