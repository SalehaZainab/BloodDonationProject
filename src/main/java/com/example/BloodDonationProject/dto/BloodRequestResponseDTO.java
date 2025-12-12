package com.example.BloodDonationProject.dto;

import java.time.LocalDateTime;

import com.example.BloodDonationProject.util.RequestStatus;
import com.example.BloodDonationProject.util.UrgencyLevel;

public class BloodRequestResponseDTO {
    private String id;
    private Long userId;
    private String bloodGroup;
    private int units;
    private UrgencyLevel urgency;
    private String hospital;
    private String city;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
