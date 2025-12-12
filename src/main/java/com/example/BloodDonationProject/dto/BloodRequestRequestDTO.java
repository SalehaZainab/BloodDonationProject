package com.example.BloodDonationProject.dto;

import com.example.BloodDonationProject.util.RequestStatus;
import com.example.BloodDonationProject.util.UrgencyLevel;
import com.example.BloodDonationProject.util.validation.ValidBloodGroup;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating blood requests.
 * All users can create blood requests as recipients asking for blood.
 * Status is ignored - always set to PENDING on backend.
 * MATCH_FOUND is set automatically when donor accepts the request.
 */
public class BloodRequestRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Blood group is required")
    @ValidBloodGroup
    private String bloodGroup;

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
}
