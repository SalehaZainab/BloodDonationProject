package com.example.BloodDonationProject.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.BloodDonationProject.util.RequestStatus;
import com.example.BloodDonationProject.util.UrgencyLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blood_request")
public class BloodRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long requestId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false, length = 10)
	private String bloodGroup;

	@Column(nullable = false)
	private int units;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private UrgencyLevel urgency;

	@Column(nullable = false, length = 255)
	private String hospital;

	@Column(nullable = false, length = 100)
	private String city;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private RequestStatus status = RequestStatus.PENDING;  // Default to PENDING

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	// Getters and setters
	public Long getRequestId() { return requestId; }
	public void setRequestId(Long requestId) { this.requestId = requestId; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
	public String getBloodGroup() { return bloodGroup; }
	public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
	public int getUnits() { return units; }
	public void setUnits(int units) { this.units = units; }
	public UrgencyLevel getUrgency() { return urgency; }
	public void setUrgency(UrgencyLevel urgency) { this.urgency = urgency; }
	public String getHospital() { return hospital; }
	public void setHospital(String hospital) { this.hospital = hospital; }
	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }
	public RequestStatus getStatus() { return status; }
	public void setStatus(RequestStatus status) { this.status = status; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
