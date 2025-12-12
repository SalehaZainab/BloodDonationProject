package com.example.BloodDonationProject.util;

public enum UrgencyLevel {
	LOW("LOW"),
	MEDIUM("MEDIUM"),
	HIGH("HIGH");

	private final String value;

	UrgencyLevel(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static UrgencyLevel fromString(String value) {
		for (UrgencyLevel level : UrgencyLevel.values()) {
			if (level.value.equalsIgnoreCase(value)) {
				return level;
			}
		}
		throw new IllegalArgumentException("Invalid urgency level: " + value);
	}
}
