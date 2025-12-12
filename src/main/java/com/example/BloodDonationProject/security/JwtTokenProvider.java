package com.example.BloodDonationProject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final int IV_LENGTH = 16;
    private static final int GCM_TAG_LENGTH = 128;

    @Value("${encryption.algorithm:AES/GCM/NoPadding}")
    private String algorithm;

    @Value("${encryption.salt:myVerySecretKeyForEncryptionThatIsExactly32Characters!!}")
    private String salt;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generate encrypted token from user ID
     */
    public String generateToken(String userId) {
        try {
            long now = System.currentTimeMillis() / 1000; // current time in seconds
            long expiresIn = now + 60 * 60 * 24; // 1 day

            // Create payload with expiration
            Map<String, Object> payloadWithExpiry = new HashMap<>();
            payloadWithExpiry.put("payload", userId);
            payloadWithExpiry.put("expiresIn", expiresIn);
            payloadWithExpiry.put("issuedAt", now);

            String jsonPayload = objectMapper.writeValueAsString(payloadWithExpiry);

            // Encrypt the payload
            return encrypt(jsonPayload);

        } catch (Exception e) {
            throw new RuntimeException("Error generating token: " + e.getMessage(), e);
        }
    }

    /**
     * Encrypt data using AES-GCM (matches Node.js crypto.createCipheriv)
     */
    private String encrypt(String data) {
        try {
            // Generate random IV (like randomBytes in Node.js)
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            // Create cipher
            Cipher cipher = Cipher.getInstance(algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), 0, 32, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            // Encrypt
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Return IV + encrypted data as hex (matches Node.js implementation)
            return bytesToHex(iv) + bytesToHex(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Encryption error: " + e.getMessage(), e);
        }
    }

    /**
     * Decrypt token
     */
    private String decrypt(String encryptedData) {
        try {
            // Extract IV (first 32 hex characters = 16 bytes)
            String ivHex = encryptedData.substring(0, 32);
            String encryptedHex = encryptedData.substring(32);

            byte[] iv = hexToBytes(ivHex);
            byte[] encrypted = hexToBytes(encryptedHex);

            // Create cipher
            Cipher cipher = Cipher.getInstance(algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), 0, 32, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            // Decrypt
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Decryption error: " + e.getMessage(), e);
        }
    }

    /**
     * Get user ID from token
     */
    public String getUserIdFromToken(String token) {
        try {
            String decrypted = decrypt(token);
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = objectMapper.readValue(decrypted, Map.class);
            return (String) payload.get("payload");
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage(), e);
        }
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token) {
        try {
            String decrypted = decrypt(token);
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = objectMapper.readValue(decrypted, Map.class);

            long now = System.currentTimeMillis() / 1000;
            Object expiresInObj = payload.get("expiresIn");

            if (expiresInObj != null) {
                long expiresIn = ((Number) expiresInObj).longValue();
                if (now >= expiresIn) {
                    System.out.println("Token expired");
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convert bytes to hex string
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Convert hex string to bytes
     */
    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
