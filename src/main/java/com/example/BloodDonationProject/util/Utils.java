package com.example.BloodDonationProject.util;

import java.security.SecureRandom;

public class Utils {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generate a 6-digit OTP
     */
    public static String OTPGenerator() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
