package com.example.BloodDonationProject.constants;

/**
 * API response messages and status codes
 */
public class ResponseMessages {

    // Status codes
    public static final int SIGNUP_SUCCESS = 10000;
    public static final int LOGIN_SUCCESS = 10001;
    public static final int VERIFICATION_SUCCESS = 10002;
    public static final int OTP_SENT_SUCCESS = 10003;
    public static final int OTP_VERIFIED_SUCCESS = 10004;
    public static final int PASSWORD_RESET_SUCCESS = 10005;
    public static final int PROFILE_UPDATE_SUCCESS = 10006;
    public static final int ACCOUNT_DELETE_SUCCESS = 10007;
    public static final int LOGOUT_SUCCESS = 10008;

    // Success messages
    public static final String SIGNUP_MESSAGE = "You have been sent a verification email";
    public static final String LOGIN_MESSAGE = "Login successfully";
    public static final String ACCOUNT_VERIFIED = "Account verified successfully";
    public static final String OTP_SENT = "OTP sent to your email";
    public static final String OTP_VERIFIED = "OTP verified successfully";
    public static final String PASSWORD_RESET = "Password reset successfully";
    public static final String PROFILE_UPDATED = "Profile updated successfully";
    public static final String ACCOUNT_DELETED = "Account deleted successfully";
    public static final String LOGOUT_MESSAGE = "Logout successful";

    // Error messages
    public static final String EMAIL_EXISTS = "Email already in use";
    public static final String USER_NOT_FOUND = "User doesn't exist";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String INVALID_OTP = "Invalid or expired OTP";
    public static final String OTP_EXPIRED = "OTP has expired";
    public static final String ACCOUNT_NOT_VERIFIED = "Please verify your account first";
    public static final String ACCOUNT_INACTIVE = "Your account is inactive";

    private ResponseMessages() {
        // Private constructor to prevent instantiation
    }
}
