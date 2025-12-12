package com.example.BloodDonationProject.util;

public enum RequestStatus {
	PENDING("PENDING"),
	MATCH_FOUND("MATCH_FOUND"),
	COMPLETED("COMPLETED"),
	CANCELLED("CANCELLED");

	private final String value;

	RequestStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static RequestStatus fromString(String value) {
		for (RequestStatus status : RequestStatus.values()) {
			if (status.value.equalsIgnoreCase(value)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Invalid request status: " + value);
	}
}
