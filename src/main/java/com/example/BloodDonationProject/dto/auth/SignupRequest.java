package com.example.BloodDonationProject.dto.auth;

import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.UserRole;
import jakarta.validation.constraints.*;

public class SignupRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    private String phone;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    private UserRole role = UserRole.USER;

    // Constructors
    public SignupRequest() {
    }

    public SignupRequest(String name, String email, String password, String phone, String city, BloodGroup bloodGroup) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
