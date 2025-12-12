package com.example.BloodDonationProject.dto;

import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @JsonProperty("_id")
    private String id;
    private String name;
    private String email;
    private String phone;
    private String city;
    private BloodGroup bloodGroup;
    private UserRole role;
    private Boolean isActive;
    private Boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
