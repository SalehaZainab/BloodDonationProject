package com.example.BloodDonationProject.dto.auth;

import com.example.BloodDonationProject.entity.BloodGroup;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    private String phone;

    private String city;

    private BloodGroup bloodGroup;

    // Constructors
    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String name, String phone, String city, BloodGroup bloodGroup) {
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.bloodGroup = bloodGroup;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
