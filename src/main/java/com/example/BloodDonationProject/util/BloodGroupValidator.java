package com.example.BloodDonationProject.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Validator for blood group values.
 * Ensures only valid blood groups are used in the system.
 */
public class BloodGroupValidator {
    private static final Set<String> VALID_BLOOD_GROUPS = new HashSet<>(Arrays.asList(
        "O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"
    ));

    /**
     * Check if the given blood group is valid.
     * @param bloodGroup the blood group to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidBloodGroup(String bloodGroup) {
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            return false;
        }
        return VALID_BLOOD_GROUPS.contains(bloodGroup.trim().toUpperCase());
    }

    /**
     * Get the list of valid blood groups.
     * @return set of valid blood groups
     */
    public static Set<String> getValidBloodGroups() {
        return new HashSet<>(VALID_BLOOD_GROUPS);
    }

    /**
     * Normalize blood group to uppercase.
     * @param bloodGroup the blood group to normalize
     * @return normalized blood group or null if invalid
     */
    public static String normalize(String bloodGroup) {
        if (bloodGroup == null) {
            return null;
        }
        String normalized = bloodGroup.trim().toUpperCase();
        return isValidBloodGroup(normalized) ? normalized : null;
    }
}
