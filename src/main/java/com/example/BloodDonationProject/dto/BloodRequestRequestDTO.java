package com.example.BloodDonationProject.dto;

import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.util.RequestStatus;
import com.example.BloodDonationProject.util.UrgencyLevel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating blood requests.
 * All users can create blood requests as recipients asking for blood.
 * Status is ignored - always set to PENDING on backend.
 * MATCH_FOUND is set automatically when donor accepts the request.
 */
public class BloodRequestRequestDTO {
    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    @Min(value = 1, message = "Units must be greater than 0")
    private int units;

    @NotNull(message = "Urgency level is required")
    private UrgencyLevel urgency;

    @NotBlank(message = "Hospital name is required")
    private String hospital;

    @NotBlank(message = "City is required")
    private String city;

    /**
     * Status field is IGNORED during creation - always set to PENDING on backend.
     * Only used for updates via PUT endpoint to manage request lifecycle.
     * Valid values: PENDING, MATCH_FOUND, COMPLETED, CANCELLED
     */
    private RequestStatus status;

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public void setUrgency(UrgencyLevel urgency) {
        this.urgency = urgency;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
