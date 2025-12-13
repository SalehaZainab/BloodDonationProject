package com.example.BloodDonationProject.controller;

import com.example.BloodDonationProject.dto.donation.DonationHistoryRequest;
import com.example.BloodDonationProject.dto.donation.DonationHistoryResponse;
import com.example.BloodDonationProject.security.RequireAuth;
import com.example.BloodDonationProject.service.DonationHistoryService;
import com.example.BloodDonationProject.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donation-history")
@RequiredArgsConstructor
public class DonationHistoryController {

    private final DonationHistoryService donationHistoryService;

    /**
     * Create a new donation history record
     */
    @PostMapping
    @RequireAuth
    public ResponseEntity<ApiResponse<DonationHistoryResponse>> createDonationHistory(
            @RequestBody DonationHistoryRequest request) {
        DonationHistoryResponse response = donationHistoryService.createDonationHistory(request);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donation history created successfully",
                response));
    }

    /**
     * Get all donation histories
     */
    @GetMapping
    @RequireAuth
    public ResponseEntity<ApiResponse<List<DonationHistoryResponse>>> getAllDonationHistories() {
        List<DonationHistoryResponse> donations = donationHistoryService.getAllDonationHistories();
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donation histories fetched successfully",
                donations));
    }

    /**
     * Get donation history by ID
     */
    @GetMapping("/{id}")
    @RequireAuth
    public ResponseEntity<ApiResponse<DonationHistoryResponse>> getDonationHistoryById(@PathVariable String id) {
        DonationHistoryResponse donation = donationHistoryService.getDonationHistoryById(id);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donation history fetched successfully",
                donation));
    }

    /**
     * Get all donations by donor ID
     */
    @GetMapping("/donor/{donorId}")
    @RequireAuth
    public ResponseEntity<ApiResponse<List<DonationHistoryResponse>>> getDonationsByDonor(
            @PathVariable String donorId) {
        List<DonationHistoryResponse> donations = donationHistoryService.getDonationsByDonor(donorId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donations by donor fetched successfully",
                donations));
    }

    /**
     * Get all donations by recipient ID
     */
    @GetMapping("/recipient/{recipientId}")
    @RequireAuth
    public ResponseEntity<ApiResponse<List<DonationHistoryResponse>>> getDonationsByRecipient(
            @PathVariable String recipientId) {
        List<DonationHistoryResponse> donations = donationHistoryService.getDonationsByRecipient(recipientId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donations by recipient fetched successfully",
                donations));
    }

    /**
     * Get donations by blood request ID
     */
    @GetMapping("/blood-request/{bloodRequestId}")
    @RequireAuth
    public ResponseEntity<ApiResponse<List<DonationHistoryResponse>>> getDonationsByBloodRequest(
            @PathVariable String bloodRequestId) {
        List<DonationHistoryResponse> donations = donationHistoryService.getDonationsByBloodRequest(bloodRequestId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donations by blood request fetched successfully",
                donations));
    }

    /**
     * Get donations by city
     */
    @GetMapping("/city/{city}")
    @RequireAuth
    public ResponseEntity<ApiResponse<List<DonationHistoryResponse>>> getDonationsByCity(@PathVariable String city) {
        List<DonationHistoryResponse> donations = donationHistoryService.getDonationsByCity(city);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donations by city fetched successfully",
                donations));
    }

    /**
     * Get donation count by donor
     */
    @GetMapping("/donor/{donorId}/count")
    @RequireAuth
    public ResponseEntity<ApiResponse<Map<String, Long>>> getDonationCountByDonor(@PathVariable String donorId) {
        long count = donationHistoryService.getDonationCountByDonor(donorId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donation count fetched successfully",
                Map.of("count", count)));
    }

    /**
     * Get donations within date range
     */
    @GetMapping("/date-range")
    @RequireAuth
    public ResponseEntity<ApiResponse<List<DonationHistoryResponse>>> getDonationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<DonationHistoryResponse> donations = donationHistoryService.getDonationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donations by date range fetched successfully",
                donations));
    }

    /**
     * Get recent donations by donor (last 30 days)
     */
    @GetMapping("/donor/{donorId}/recent")
    @RequireAuth
    public ResponseEntity<ApiResponse<List<DonationHistoryResponse>>> getRecentDonationsByDonor(
            @PathVariable String donorId) {
        List<DonationHistoryResponse> donations = donationHistoryService.getRecentDonationsByDonor(donorId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Recent donations fetched successfully",
                donations));
    }

    /**
     * Update donation history
     */
    @PutMapping("/{id}")
    @RequireAuth
    public ResponseEntity<ApiResponse<DonationHistoryResponse>> updateDonationHistory(
            @PathVariable String id,
            @RequestBody DonationHistoryRequest request) {
        DonationHistoryResponse response = donationHistoryService.updateDonationHistory(id, request);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donation history updated successfully",
                response));
    }

    /**
     * Delete donation history (soft delete)
     */
    @DeleteMapping("/{id}")
    @RequireAuth
    public ResponseEntity<ApiResponse<Object>> deleteDonationHistory(@PathVariable String id) {
        donationHistoryService.deleteDonationHistory(id);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Donation history deleted successfully"));
    }

    /**
     * Get my donation history (for authenticated donor)
     */
    @GetMapping("/my-donations")
    @RequireAuth
    public ResponseEntity<ApiResponse<List<DonationHistoryResponse>>> getMyDonations(
            @RequestAttribute("userId") String userId) {
        List<DonationHistoryResponse> donations = donationHistoryService.getDonationsByDonor(userId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "My donations fetched successfully",
                donations));
    }

    /**
     * Get my donation count (for authenticated donor)
     */
    @GetMapping("/my-donations/count")
    @RequireAuth
    public ResponseEntity<ApiResponse<Map<String, Long>>> getMyDonationCount(
            @RequestAttribute("userId") String userId) {
        long count = donationHistoryService.getDonationCountByDonor(userId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "My donation count fetched successfully",
                Map.of("count", count)));
    }
}
