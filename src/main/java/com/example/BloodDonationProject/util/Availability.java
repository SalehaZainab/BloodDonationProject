package com.example.BloodDonationProject.util;

public enum Availability {
	AVAILABLE("AVAILABLE"),
	NOT_AVAILABLE("NOT_AVAILABLE");

	private final String value;

	Availability(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static Availability fromString(String value) {
		for (Availability status : Availability.values()) {
			if (status.value.equalsIgnoreCase(value)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Invalid availability: " + value);
	}
}
