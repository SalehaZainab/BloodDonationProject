package com.example.BloodDonationProject.util;

/**
 * Standardized API response structure
 */
public class ApiResponse<T> {
    private boolean status;
    private int statusNumber;
    private String message;
    private T data;

    // Constructors
    public ApiResponse() {
    }

    public ApiResponse(boolean status, int statusNumber, String message, T data) {
        this.status = status;
        this.statusNumber = statusNumber;
        this.message = message;
        this.data = data;
    }

    // Success response with data
    public static <T> ApiResponse<T> success(int statusNumber, String message, T data) {
        return new ApiResponse<>(true, statusNumber, message, data);
    }

    // Success response without data
    public static <T> ApiResponse<T> success(int statusNumber, String message) {
        return new ApiResponse<>(true, statusNumber, message, null);
    }

    // Error response
    public static <T> ApiResponse<T> error(int statusNumber, String message) {
        return new ApiResponse<>(false, statusNumber, message, null);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
