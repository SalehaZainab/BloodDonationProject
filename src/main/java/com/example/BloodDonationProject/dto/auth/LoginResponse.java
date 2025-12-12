package com.example.BloodDonationProject.dto.auth;

import com.example.BloodDonationProject.dto.UserResponse;

public class LoginResponse {

    private boolean status;
    private int statusNumber;
    private String message;
    private LoginData data;

    // Constructors
    public LoginResponse() {
    }

    public LoginResponse(boolean status, int statusNumber, String message, LoginData data) {
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

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    // Inner class for data
    public static class LoginData {
        private UserResponse user;
        private String token;

        public LoginData() {
        }

        public LoginData(UserResponse user, String token) {
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
