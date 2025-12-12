package com.example.BloodDonationProject.dto.auth;

import com.example.BloodDonationProject.dto.UserResponse;

public class SignupResponse {

    private boolean status;
    private int statusNumber;
    private String message;
    private SignupData data;

    // Constructors
    public SignupResponse() {
    }

    public SignupResponse(boolean status, int statusNumber, String message, SignupData data) {
        this.status = status;
        this.statusNumber = statusNumber;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SignupData getData() {
        return data;
    }

    public void setData(SignupData data) {
        this.data = data;
    }

    // Inner class for data
    public static class SignupData {
        private UserResponse user;
        private String token;

        public SignupData() {
        }

        public SignupData(UserResponse user, String token) {
            this.user = user;
            this.token = token;
        }

        public UserResponse getUser() {
            return user;
        }

        public void setUser(UserResponse user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
