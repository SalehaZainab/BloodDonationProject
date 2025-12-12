package com.example.BloodDonationProject.dto;

import com.example.BloodDonationProject.entity.BloodGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;
}
