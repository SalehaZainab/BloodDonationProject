package com.example.BloodDonationProject.controller;

import com.example.BloodDonationProject.dto.UserResponse;
import com.example.BloodDonationProject.dto.auth.*;
import com.example.BloodDonationProject.security.RequireAuth;
import com.example.BloodDonationProject.service.AuthService;
import com.example.BloodDonationProject.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Signup - Register a new user
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Login - Authenticate user
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Update Profile
     */
    @RequireAuth
    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable String userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        ApiResponse<UserResponse> response = authService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Verify Account with OTP
     */
    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse<Object>> verifyAccount(@Valid @RequestBody VerifyOtpRequest request) {
        ApiResponse<Object> response = authService.verifyAccount(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Forgot Password - Send OTP
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        ApiResponse<Object> response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Resend OTP
     */
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Object>> resendOtp(@Valid @RequestBody ForgotPasswordRequest request) {
        ApiResponse<Object> response = authService.resendOtp(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Verify OTP for password reset
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        ApiResponse<Object> response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Reset Password with OTP
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ApiResponse<Object> response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete Account
     */
    @RequireAuth
    @DeleteMapping("/account/{userId}")
    public ResponseEntity<ApiResponse<Object>> deleteAccount(@PathVariable String userId) {
        ApiResponse<Object> response = authService.deleteAccount(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout
     */
    @RequireAuth
    @PostMapping("/logout/{userId}")
    public ResponseEntity<ApiResponse<Object>> logout(@PathVariable String userId) {
        ApiResponse<Object> response = authService.logout(userId);
        return ResponseEntity.ok(response);
    }
}
