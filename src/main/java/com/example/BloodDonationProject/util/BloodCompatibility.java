package com.example.BloodDonationProject.util;

public class BloodCompatibility {
	/**
	 * Return donor blood groups compatible with the given recipient group.
	 * Result groups are uppercase.
	 */
	public static java.util.List<String> compatibleDonorGroups(String recipientGroup) {
		if (recipientGroup == null) return java.util.Collections.emptyList();
		String g = recipientGroup.trim().toUpperCase();
		switch (g) {
			case "O-": return java.util.List.of("O-");
			case "O+": return java.util.List.of("O-", "O+");
			case "A-": return java.util.List.of("O-", "A-");
			case "A+": return java.util.List.of("O-", "O+", "A-", "A+");
			case "B-": return java.util.List.of("O-", "B-");
			case "B+": return java.util.List.of("O-", "O+", "B-", "B+");
			case "AB-": return java.util.List.of("O-", "A-", "B-", "AB-");
			case "AB+": return java.util.List.of("O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+");
			default: return java.util.List.of(g);
		}
	}
}
