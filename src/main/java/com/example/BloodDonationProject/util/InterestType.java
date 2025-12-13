package com.example.BloodDonationProject.util;

/**
 * Types of donation interest
 */
public enum InterestType {
    /**
     * Donor sees a blood request and offers to donate
     * Flow: Donor → Blood Request
     */
    DONOR_TO_REQUEST,

    /**
     * Requester sees a donor profile and asks them to donate
     * Flow: Requester → Donor Profile
     */
    REQUESTER_TO_DONOR
}
