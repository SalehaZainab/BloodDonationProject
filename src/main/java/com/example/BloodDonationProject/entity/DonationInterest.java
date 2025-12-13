package com.example.BloodDonationProject.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.example.BloodDonationProject.util.InterestType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * DonationInterest entity - Link between DonorProfile and BloodRequest
 * Represents interest between donors and requesters
 * 
 * Two scenarios:
 * 1. DONOR_TO_REQUEST: Donor sees blood request and offers to donate
 * 2. REQUESTER_TO_DONOR: Requester sees donor profile and asks for donation
 * 
 * One record = One donor + one blood request + interest type + status
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

	@Enumerated(EnumType.STRING)
	@Column(name = "interest_type", nullable = false, length = 20)
	private InterestType interestType; // DONOR_TO_REQUEST or REQUESTER_TO_DONOR

	@Column(name = "status", nullable = false, length = 20)
	private String status = "PENDING"; // Default to PENDING

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	// Constructors
	public DonationInterest() {
	}

	public DonationInterest(DonorProfile donor, BloodRequest request, InterestType interestType) {
		this.donor = donor;
		this.request = request;
		this.interestType = interestType;
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

	public InterestType getInterestType() {
		return interestType;
	}

	public void setInterestType(InterestType interestType) {
		this.interestType = interestType;
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
