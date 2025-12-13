package com.example.BloodDonationProject.service;

import com.example.BloodDonationProject.dto.donation.DonationHistoryRequest;
import com.example.BloodDonationProject.dto.donation.DonationHistoryResponse;
import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.DonationHistory;
import com.example.BloodDonationProject.entity.User;
import com.example.BloodDonationProject.repository.DonationHistoryRepository;
import com.example.BloodDonationProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationHistoryService {

    private final DonationHistoryRepository donationHistoryRepository;
    private final UserRepository userRepository;

    /**
     * Create a new donation history record
     */
    @Transactional
    public DonationHistoryResponse createDonationHistory(DonationHistoryRequest request) {
        // Validate donor exists
        User donor = userRepository.findByIdAndDeletedAtIsNull(request.getDonorId())
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + request.getDonorId()));

        // Validate recipient if provided
        if (request.getRecipientId() != null) {
            userRepository.findByIdAndDeletedAtIsNull(request.getRecipientId())
                    .orElseThrow(
                            () -> new RuntimeException("Recipient not found with id: " + request.getRecipientId()));
        }

        DonationHistory donation = new DonationHistory();
        donation.setId(UUID.randomUUID().toString());
        donation.setDonorId(request.getDonorId());
        donation.setRecipientId(request.getRecipientId());
        donation.setBloodRequestId(request.getBloodRequestId());
        donation.setBloodGroup(request.getBloodGroup());
        donation.setUnits(request.getUnits());
        donation.setDonationDate(request.getDonationDate() != null ? request.getDonationDate() : LocalDateTime.now());
        donation.setHospital(request.getHospital());
        donation.setCity(request.getCity());
        donation.setStatus(request.getStatus());
        donation.setNotes(request.getNotes());

        DonationHistory saved = donationHistoryRepository.save(donation);
        return mapToResponse(saved);
    }

    /**
     * Get all donation histories
     */
    public List<DonationHistoryResponse> getAllDonationHistories() {
        return donationHistoryRepository.findByDeletedAtIsNullOrderByDonationDateDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get donation history by ID
     */
    public DonationHistoryResponse getDonationHistoryById(String id) {
        DonationHistory donation = donationHistoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Donation history not found with id: " + id));
        return mapToResponse(donation);
    }

    /**
     * Get all donations by donor ID
     */
    public List<DonationHistoryResponse> getDonationsByDonor(String donorId) {
        // Validate donor exists
        userRepository.findByIdAndDeletedAtIsNull(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + donorId));

        return donationHistoryRepository.findByDonorIdAndDeletedAtIsNullOrderByDonationDateDesc(donorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all donations by recipient ID
     */
    public List<DonationHistoryResponse> getDonationsByRecipient(String recipientId) {
        // Validate recipient exists
        userRepository.findByIdAndDeletedAtIsNull(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found with id: " + recipientId));

        return donationHistoryRepository.findByRecipientIdAndDeletedAtIsNullOrderByDonationDateDesc(recipientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get donations by blood request ID
     */
    public List<DonationHistoryResponse> getDonationsByBloodRequest(String bloodRequestId) {
        return donationHistoryRepository.findByBloodRequestIdAndDeletedAtIsNull(bloodRequestId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get donations by city
     */
    public List<DonationHistoryResponse> getDonationsByCity(String city) {
        return donationHistoryRepository.findByCityAndDeletedAtIsNullOrderByDonationDateDesc(city)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get donation count by donor
     */
    public long getDonationCountByDonor(String donorId) {
        // Validate donor exists
        userRepository.findByIdAndDeletedAtIsNull(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + donorId));

        return donationHistoryRepository.countByDonorId(donorId);
    }

    /**
     * Get donations within date range
     */
    public List<DonationHistoryResponse> getDonationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return donationHistoryRepository.findByDonationDateBetween(startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get recent donations by donor (last 30 days)
     */
    public List<DonationHistoryResponse> getRecentDonationsByDonor(String donorId) {
        // Validate donor exists
        userRepository.findByIdAndDeletedAtIsNull(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + donorId));

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return donationHistoryRepository.findRecentDonationsByDonor(donorId, thirtyDaysAgo)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update donation history
     */
    @Transactional
    public DonationHistoryResponse updateDonationHistory(String id, DonationHistoryRequest request) {
        DonationHistory donation = donationHistoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Donation history not found with id: " + id));

        // Update only provided fields
        if (request.getRecipientId() != null) {
            userRepository.findByIdAndDeletedAtIsNull(request.getRecipientId())
                    .orElseThrow(
                            () -> new RuntimeException("Recipient not found with id: " + request.getRecipientId()));
            donation.setRecipientId(request.getRecipientId());
        }
        if (request.getBloodRequestId() != null) {
            donation.setBloodRequestId(request.getBloodRequestId());
        }
        if (request.getBloodGroup() != null) {
            donation.setBloodGroup(request.getBloodGroup());
        }
        if (request.getUnits() != null) {
            donation.setUnits(request.getUnits());
        }
        if (request.getDonationDate() != null) {
            donation.setDonationDate(request.getDonationDate());
        }
        if (request.getHospital() != null) {
            donation.setHospital(request.getHospital());
        }
        if (request.getCity() != null) {
            donation.setCity(request.getCity());
        }
        if (request.getStatus() != null) {
            donation.setStatus(request.getStatus());
        }
        if (request.getNotes() != null) {
            donation.setNotes(request.getNotes());
        }

        DonationHistory updated = donationHistoryRepository.save(donation);
        return mapToResponse(updated);
    }

    /**
     * Delete donation history (soft delete)
     */
    @Transactional
    public void deleteDonationHistory(String id) {
        DonationHistory donation = donationHistoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Donation history not found with id: " + id));

        donation.setDeletedAt(LocalDateTime.now());
        donationHistoryRepository.save(donation);
    }

    /**
     * Get donations by blood group
     */
    public List<DonationHistoryResponse> getDonationsByBloodGroup(BloodGroup bloodGroup) {
        return donationHistoryRepository.findByBloodGroupAndDeletedAtIsNullOrderByDonationDateDesc(bloodGroup)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Filter donations with multiple optional criteria
     */
    public List<DonationHistoryResponse> filterDonations(String donorId, String recipientId,
            String bloodRequestId, String city, BloodGroup bloodGroup) {

        List<DonationHistory> donations = donationHistoryRepository.findByDeletedAtIsNullOrderByDonationDateDesc();

        return donations.stream()
                .filter(d -> donorId == null || d.getDonorId().equals(donorId))
                .filter(d -> recipientId == null
                        || d.getRecipientId() != null && d.getRecipientId().equals(recipientId))
                .filter(d -> bloodRequestId == null
                        || d.getBloodRequestId() != null && d.getBloodRequestId().equals(bloodRequestId))
                .filter(d -> city == null || d.getCity().equalsIgnoreCase(city))
                .filter(d -> bloodGroup == null || d.getBloodGroup() == bloodGroup)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Map DonationHistory entity to response DTO
     */
    private DonationHistoryResponse mapToResponse(DonationHistory donation) {
        DonationHistoryResponse response = new DonationHistoryResponse();
        response.setId(donation.getId());
        response.setDonorId(donation.getDonorId());
        response.setRecipientId(donation.getRecipientId());
        response.setBloodRequestId(donation.getBloodRequestId());
        response.setBloodGroup(donation.getBloodGroup());
        response.setUnits(donation.getUnits());
        response.setDonationDate(donation.getDonationDate());
        response.setHospital(donation.getHospital());
        response.setCity(donation.getCity());
        response.setStatus(donation.getStatus());
        response.setNotes(donation.getNotes());
        response.setCreatedAt(donation.getCreatedAt());
        response.setUpdatedAt(donation.getUpdatedAt());
        return response;
    }
}
