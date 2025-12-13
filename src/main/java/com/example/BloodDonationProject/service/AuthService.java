package com.example.BloodDonationProject.service;

import com.example.BloodDonationProject.constants.ResponseMessages;
import com.example.BloodDonationProject.dto.UserResponse;
import com.example.BloodDonationProject.dto.auth.*;
import com.example.BloodDonationProject.entity.Otp;
import com.example.BloodDonationProject.entity.User;
import com.example.BloodDonationProject.entity.UserRole;
import com.example.BloodDonationProject.repository.OtpRepository;
import com.example.BloodDonationProject.repository.UserRepository;
import com.example.BloodDonationProject.security.JwtTokenProvider;
import com.example.BloodDonationProject.util.ApiResponse;
import com.example.BloodDonationProject.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    // TODO: Add PasswordEncoder when implementing security

    private static final int OTP_EXPIRY_IN_MINUTES = 10;

    /**
     * Register a new user (Signup)
     */
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // Extract and sanitize data
        String email = request.getEmail().toLowerCase();
        String password = request.getPassword();
        String phone = request.getPhone();
        String name = request.getName();
        String city = request.getCity();

        System.out.println("role: " + request.getRole());

        // Check if email already exists
        if (userRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new RuntimeException("Email already in use");
        }

        // Hash password
        // TODO: Replace with BCrypt hashing
        String hashedPassword = password; // cryptoHash(password);

        // Generate OTP
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime otpExpiresIn = currentTime.plusMinutes(OTP_EXPIRY_IN_MINUTES);

        String otp = Utils.OTPGenerator();
        System.out.println("Generated OTP: " + otp);

        // Create new user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setPhone(phone);
        user.setCity(city);
        user.setBloodGroup(request.getBloodGroup());
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.USER);
        user.setIsActive(false); // Account needs verification

        // Save user
        User savedUser = userRepository.save(user);

        // Save OTP
        saveOtp(savedUser.getEmail(), otp, otpExpiresIn);

        // Send verification email
        System.out.println("\n🔄 Attempting to send OTP email...");
        try {
            emailService.sendOtpEmail(savedUser.getEmail(), otp, savedUser.getName());
            System.out.println("✅ OTP email sent successfully to: " + email);
        } catch (Exception e) {
            System.err.println("❌ FAILED to send OTP email!");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getName());
            e.printStackTrace();
            // Continue with registration even if email fails
        }

        System.out.println("Verification process completed for: " + email);

        // Get sanitized user data
        UserResponse userData = mapToResponse(savedUser);
        System.out.println("userData: " + userData.getEmail());

        // Create nested data structure
        SignupResponse.SignupData signupData = new SignupResponse.SignupData(
                userData,
                null // Token will be null until account is verified
        );

        // Return response
        SignupResponse response = new SignupResponse(
                true,
                10000, // Status number for signup
                "You have been sent a verification email",
                signupData);

        return response;
    }

    /**
     * User login
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();
            // String deviceToken = request.getDeviceToken(); // TODO: Add to LoginRequest
            // String deviceType = request.getDeviceType(); // TODO: Add to LoginRequest

            // Authenticate user
            AuthResult authResult = authenticateUser(email, password);

            if (!authResult.isSuccess()) {
                throw new RuntimeException(authResult.getMessage());
            }

            User user = authResult.getUser();

            if (user == null) {
                throw new RuntimeException("Authentication failed");
            }

            // Check if account is verified
            if (!user.IsActive()) {
                throw new RuntimeException(ResponseMessages.ACCOUNT_NOT_VERIFIED);
            }

            // TODO: Handle device token and device type
            // if (deviceToken != null && deviceType != null) {
            // updateUserDevice(user, deviceToken, deviceType);
            // }

            // Get sanitized user data
            UserResponse userData = mapToResponse(user);

            // Generate token
            String token = generateToken(userData);

            System.out.println("Login successful for user: " + userData.getEmail());

            // Create nested data structure
            LoginResponse.LoginData loginData = new LoginResponse.LoginData(userData, token);

            return new LoginResponse(true, 10001, "Login successfully", loginData);

        } catch (RuntimeException error) {
            System.out.println("Login error: " + error.getMessage());
            throw error; // Re-throw the original error with specific message
        } catch (Exception error) {
            System.out.println("Login error: " + error.getMessage());
            throw new RuntimeException("Something went wrong during login");
        }
    }

    /**
     * Authenticate user credentials
     */
    private AuthResult authenticateUser(String email, String password) {
        try {
            User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                    .orElse(null);

            if (user == null) {
                return new AuthResult(false, "User doesn't exist", null);
            }

            // Hash password and compare
            // TODO: Replace with BCrypt verification
            String derivedKey = password; // cryptoHash(password);

            if (!user.getPassword().equals(derivedKey)) {
                return new AuthResult(false, "Invalid credentials", null);
            }

            // Check if account is verified
            if (!user.IsActive()) {
                return new AuthResult(false, ResponseMessages.ACCOUNT_NOT_VERIFIED, null);
            }

            return new AuthResult(true, "Authentication successful", user);

        } catch (Exception error) {
            System.out.println("Authentication error: " + error.getMessage());
            return new AuthResult(false, error.getMessage(), null);
        }
    }

    /**
     * Generate JWT token for user
     */
    private String generateToken(UserResponse userData) {
        return jwtTokenProvider.generateToken(userData.getId());
    }

    /**
     * Inner class for authentication result
     */
    private static class AuthResult {
        private final boolean success;
        private final String message;
        private final User user;

        public AuthResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public User getUser() {
            return user;
        }
    }

    /**
     * Get user profile by userId
     */
    public ApiResponse<UserResponse> getProfile(String userId) {
        System.out.println("🔍 getProfile called with userId: " + userId);

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> {
                    System.out.println("❌ User not found for userId: " + userId);
                    return new RuntimeException(ResponseMessages.USER_NOT_FOUND);
                });

        System.out.println("✅ User found: " + user.getEmail());
        UserResponse userData = mapToResponse(user);

        return ApiResponse.success(
                ResponseMessages.PROFILE_UPDATE_SUCCESS,
                "Profile retrieved successfully",
                userData);
    }

    /**
     * Update user profile
     */
    @Transactional
    public ApiResponse<UserResponse> updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_FOUND));

        // Update only provided fields
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user.setPhone(request.getPhone());
        }
        if (request.getCity() != null && !request.getCity().isBlank()) {
            user.setCity(request.getCity());
        }
        if (request.getBloodGroup() != null) {
            user.setBloodGroup(request.getBloodGroup());
        }

        User updatedUser = userRepository.save(user);
        UserResponse userData = mapToResponse(updatedUser);

        return ApiResponse.success(
                ResponseMessages.PROFILE_UPDATE_SUCCESS,
                ResponseMessages.PROFILE_UPDATED,
                userData);
    }

    /**
     * Verify account with OTP
     */
    @Transactional
    public ApiResponse<Object> verifyAccount(VerifyOtpRequest request) {
        // Find user
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_FOUND));

        // Verify OTP
        Otp otp = otpRepository.findByEmailAndOtpCodeAndVerifiedFalse(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new RuntimeException(ResponseMessages.INVALID_OTP));

        if (otp.isExpired()) {
            throw new RuntimeException(ResponseMessages.OTP_EXPIRED);
        }

        // Mark OTP as verified
        otp.setVerified(true);
        otpRepository.save(otp);

        // Activate user account
        user.setIsActive(true);
        userRepository.save(user);

        // Send welcome email after successful verification
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getName());
            System.out.println("✅ Welcome email sent to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("❌ Failed to send welcome email: " + e.getMessage());
            // Continue even if email fails
        }

        return ApiResponse.success(
                ResponseMessages.VERIFICATION_SUCCESS,
                ResponseMessages.ACCOUNT_VERIFIED);
    }

    /**
     * Forgot password - send OTP
     */
    @Transactional
    public ApiResponse<Object> forgotPassword(ForgotPasswordRequest request) {
        // Check if user exists
        userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_FOUND));

        // Generate and send OTP
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime otpExpiresIn = currentTime.plusMinutes(OTP_EXPIRY_IN_MINUTES);

        String otp = Utils.OTPGenerator();
        System.out.println("Generated OTP for password reset: " + otp);
        saveOtp(request.getEmail(), otp, otpExpiresIn);

        // Send OTP via email
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_FOUND));

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), otp, user.getName());
            System.out.println("Password reset OTP sent to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email. Please try again.");
        }

        return ApiResponse.success(
                ResponseMessages.OTP_SENT_SUCCESS,
                ResponseMessages.OTP_SENT);
    }

    /**
     * Verify OTP for password reset
     */
    public ApiResponse<Object> verifyOtp(VerifyOtpRequest request) {
        System.out.println("\n======= VERIFY OTP DEBUG =======");
        System.out.println("Email: " + request.getEmail());
        System.out.println("OTP: " + request.getOtp());

        // Check if OTP exists (regardless of verification status)
        Optional<Otp> anyOtp = otpRepository.findByEmailAndOtpCodeAndVerifiedFalse(request.getEmail(),
                request.getOtp());

        if (anyOtp.isEmpty()) {
            System.out.println("❌ No unverified OTP found for this email and code");
            System.out.println("Possible reasons:");
            System.out.println("1. OTP was already used/verified");
            System.out.println("2. Wrong OTP code entered");
            System.out.println("3. Wrong email entered");
            throw new RuntimeException(ResponseMessages.INVALID_OTP);
        }

        Otp otp = anyOtp.get();
        System.out.println("✅ OTP found - Created: " + otp.getCreatedAt());
        System.out.println("OTP Expiry: " + otp.getExpiryTime());
        System.out.println("Current Time: " + LocalDateTime.now());
        System.out.println("Is Expired: " + otp.isExpired());
        System.out.println("Is Verified: " + otp.isVerified());

        if (otp.isExpired()) {
            System.out.println("❌ OTP has expired");
            throw new RuntimeException(ResponseMessages.OTP_EXPIRED);
        }

        System.out.println("✅ OTP is valid and can be used for password reset");
        System.out.println("===============================\n");

        return ApiResponse.success(
                ResponseMessages.OTP_VERIFIED_SUCCESS,
                ResponseMessages.OTP_VERIFIED);
    }

    /**
     * Reset password with OTP
     */
    @Transactional
    public ApiResponse<Object> resetPassword(ResetPasswordRequest request) {
        System.out.println("\n======= RESET PASSWORD DEBUG =======");
        System.out.println("Email: " + request.getEmail());
        System.out.println("OTP: " + request.getOtp());
        System.out.println(
                "New Password Length: " + (request.getNewPassword() != null ? request.getNewPassword().length() : 0));

        // Verify OTP
        Optional<Otp> otpOptional = otpRepository.findByEmailAndOtpCodeAndVerifiedFalse(request.getEmail(),
                request.getOtp());

        if (otpOptional.isEmpty()) {
            System.out.println("❌ No unverified OTP found");
            System.out.println("Possible reasons:");
            System.out.println("1. OTP was already used for password reset");
            System.out.println("2. Wrong OTP code");
            System.out.println("3. OTP expired and was cleaned up");
            throw new RuntimeException(ResponseMessages.INVALID_OTP);
        }

        Otp otp = otpOptional.get();
        System.out.println("✅ OTP found - Created: " + otp.getCreatedAt());
        System.out.println("OTP Expiry: " + otp.getExpiryTime());
        System.out.println("Is Expired: " + otp.isExpired());

        if (otp.isExpired()) {
            System.out.println("❌ OTP has expired");
            throw new RuntimeException(ResponseMessages.OTP_EXPIRED);
        }

        // Find user and update password
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_FOUND));

        System.out.println("✅ User found: " + user.getEmail());
        System.out.println("Updating password...");

        // TODO: Hash password with BCrypt - Currently storing plain text (SECURITY
        // ISSUE)
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        System.out.println("✅ Password updated successfully");

        // Mark OTP as verified
        otp.setVerified(true);
        otpRepository.save(otp);

        System.out.println("✅ OTP marked as verified - cannot be reused");
        System.out.println("===============================\n");

        return ApiResponse.success(
                ResponseMessages.PASSWORD_RESET_SUCCESS,
                ResponseMessages.PASSWORD_RESET);
    }

    /**
     * Delete user account
     */
    @Transactional
    public ApiResponse<Object> deleteAccount(String userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_FOUND));

        // Soft delete by deactivating account
        user.setIsActive(false);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return ApiResponse.success(
                ResponseMessages.ACCOUNT_DELETE_SUCCESS,
                ResponseMessages.ACCOUNT_DELETED);
    }

    /**
     * Resend OTP for account verification or password reset
     */
    @Transactional
    public ApiResponse<Object> resendOtp(ForgotPasswordRequest request) {
        // Check if user exists
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_FOUND));

        // Generate new OTP
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime otpExpiresIn = currentTime.plusMinutes(OTP_EXPIRY_IN_MINUTES);

        String otp = Utils.OTPGenerator();
        System.out.println("Generated OTP for resend: " + otp);
        saveOtp(request.getEmail(), otp, otpExpiresIn);

        // Send OTP via email
        System.out.println("\n🔄 Attempting to resend OTP email...");
        try {
            emailService.sendOtpEmail(user.getEmail(), otp, user.getName());
            System.out.println("✅ OTP email resent successfully to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("❌ Failed to resend OTP email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email. Please try again.");
        }

        return ApiResponse.success(
                ResponseMessages.OTP_SENT_SUCCESS,
                "OTP has been resent to your email");
    }

    /**
     * Logout user
     */
    public ApiResponse<Object> logout(String userId) {
        // TODO: Invalidate JWT token (add to blacklist or remove from cache)
        return ApiResponse.success(
                ResponseMessages.LOGOUT_SUCCESS,
                ResponseMessages.LOGOUT_MESSAGE);
    }

    /**
     * Update user's lastActive timestamp
     */
    @Transactional
    public void updateLastActive(String userId) {
        Optional<User> userOptional = userRepository.findByIdAndDeletedAtIsNull(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastActive(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    // Helper methods

    private void saveOtp(String email, String otpCode, LocalDateTime expiryTime) {
        // Delete any existing OTPs for this email
        Optional<Otp> existingOtp = otpRepository.findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(email);
        existingOtp.ifPresent(otp -> otpRepository.delete(otp));

        // Create new OTP
        Otp otp = new Otp(email, otpCode, expiryTime);
        otpRepository.save(otp);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setCity(user.getCity());
        response.setBloodGroup(user.getBloodGroup());
        response.setRole(user.getRole());
        response.setIsActive(user.IsActive());
        response.setIsVerified(user.IsActive()); // Set isVerified same as isActive
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
