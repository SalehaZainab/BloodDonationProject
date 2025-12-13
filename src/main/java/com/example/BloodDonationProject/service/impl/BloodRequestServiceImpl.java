package com.example.BloodDonationProject.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BloodDonationProject.dto.BloodRequestRequestDTO;
import com.example.BloodDonationProject.dto.BloodRequestResponseDTO;
import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.BloodRequest;
import com.example.BloodDonationProject.entity.DonorProfile;
import com.example.BloodDonationProject.entity.User;
import com.example.BloodDonationProject.repository.BloodRequestRepository;
import com.example.BloodDonationProject.repository.DonorRepository;
import com.example.BloodDonationProject.repository.UserRepository;
import com.example.BloodDonationProject.service.BloodRequestService;
import com.example.BloodDonationProject.service.EmailService;
import com.example.BloodDonationProject.util.Availability;
import com.example.BloodDonationProject.util.RequestStatus;

/**
 * Service implementation for blood request management.
 * 
 * Business Logic:
 * - All users can create blood requests (they are recipients asking for blood)
 * - Status always starts as PENDING on creation
 * - Only PENDING requests can be manually updated to CANCELLED or COMPLETED
 * - MATCH_FOUND is set automatically by system when donor accepts request
 * - Blood groups validated against allowed values
 * - Units must be positive
 */
@Service
@Transactional
public class BloodRequestServiceImpl implements BloodRequestService {
    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public BloodRequestResponseDTO createRequest(BloodRequestRequestDTO dto) {
        // All users can create blood requests - they are recipients asking for blood

        // Validate blood group
        if (dto.getBloodGroup() == null) {
            throw new RuntimeException("Blood group is required");
        }

        // Create entity and enforce PENDING status (ignore any status from DTO)
        BloodRequest entity = toEntity(dto);
        entity.setStatus(RequestStatus.PENDING); // Always enforce PENDING on creation

        BloodRequest saved = bloodRequestRepository.save(entity);

        // After saving request, find matching donors and notify them
        try {
            notifyMatchingDonors(saved);
        } catch (Exception e) {
            // Log error but don't fail the request creation
            System.err.println("Failed to notify matching donors: " + e.getMessage());
            e.printStackTrace();
        }

        return toDto(saved);
    }

    @Override
    public BloodRequestResponseDTO updateRequest(String requestId, BloodRequestRequestDTO dto) {
        BloodRequest entity = bloodRequestRepository.findById(UUID.fromString(requestId))
                .orElseThrow(() -> new RuntimeException("BloodRequest not found with id: " + requestId));

        // Update fields (not allowing userId change)
        if (dto.getBloodGroup() != null) {
            BloodGroup bg = BloodGroup.valueOf(dto.getBloodGroup().name());
            entity.setBloodGroup(bg);
        }

        entity.setUnits(dto.getUnits());

        if (dto.getUrgency() != null) {
            entity.setUrgency(dto.getUrgency());
        }

        if (dto.getHospital() != null && !dto.getHospital().isBlank()) {
            entity.setHospital(dto.getHospital());
        }

        if (dto.getCity() != null && !dto.getCity().isBlank()) {
            entity.setCity(dto.getCity());
        }

        // Allow status transitions: PENDING -> MATCH_FOUND -> COMPLETED/CANCELLED
        if (dto.getStatus() != null) {
            validateStatusTransition(entity.getStatus(), dto.getStatus());
            entity.setStatus(dto.getStatus());
        }

        BloodRequest updated = bloodRequestRepository.save(entity);
        return toDto(updated);
    }

    @Override
    public BloodRequestResponseDTO getRequestById(String requestId) {
        BloodRequest entity = bloodRequestRepository.findById(UUID.fromString(requestId))
                .orElseThrow(() -> new RuntimeException("BloodRequest not found with id: " + requestId));
        return toDto(entity);
    }

    @Override
    public List<BloodRequestResponseDTO> getAllRequests() {
        return bloodRequestRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BloodRequestResponseDTO> getRequestsByStatus(RequestStatus status) {
        return bloodRequestRepository.findByStatus(status).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BloodRequestResponseDTO> getRequestsByBloodGroup(String bloodGroup) {
        try {
            BloodGroup bg = BloodGroup
                    .valueOf(bloodGroup.toUpperCase().replace("+", "_POSITIVE").replace("-", "_NEGATIVE"));
            return bloodRequestRepository.findByBloodGroup(bg).stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid blood group: " + bloodGroup);
        }
    }

    @Override
    public List<BloodRequestResponseDTO> getRequestsByCity(String city) {
        return bloodRequestRepository.findByCity(city).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BloodRequestResponseDTO> getRequestsByHospital(String hospital) {
        return bloodRequestRepository.findByHospital(hospital).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRequest(String requestId) {
        BloodRequest entity = bloodRequestRepository.findById(UUID.fromString(requestId))
                .orElseThrow(() -> new RuntimeException("BloodRequest not found with id: " + requestId));
        bloodRequestRepository.delete(entity);
    }

    /**
     * Validates status transitions.
     * 
     * Valid transitions:
     * PENDING -> CANCELLED (recipient can cancel before donor accepts)
     * MATCH_FOUND -> CANCELLED, COMPLETED (donor accepted, can now complete or
     * cancel)
     * COMPLETED -> (no transitions allowed - terminal state)
     * CANCELLED -> (no transitions allowed - terminal state)
     * 
     * Note: MATCH_FOUND is set automatically by system when donor accepts request.
     * Users cannot manually transition to MATCH_FOUND.
     * From PENDING, only CANCELLED is allowed (no COMPLETED).
     */
    private void validateStatusTransition(RequestStatus currentStatus, RequestStatus newStatus) {
        // Terminal states cannot be changed
        if (currentStatus == RequestStatus.COMPLETED || currentStatus == RequestStatus.CANCELLED) {
            throw new RuntimeException(
                    "Cannot change status of " + currentStatus + " requests. Terminal states cannot be modified.");
        }

        // From PENDING: only CANCELLED allowed
        if (currentStatus == RequestStatus.PENDING) {
            if (newStatus != RequestStatus.CANCELLED) {
                throw new RuntimeException(
                        "PENDING requests can only be CANCELLED. COMPLETED is only available after donor accepts (MATCH_FOUND status).");
            }
        }
        // From MATCH_FOUND: CANCELLED or COMPLETED allowed
        else if (currentStatus == RequestStatus.MATCH_FOUND) {
            if (newStatus != RequestStatus.CANCELLED && newStatus != RequestStatus.COMPLETED) {
                throw new RuntimeException("MATCH_FOUND requests can only be updated to CANCELLED or COMPLETED.");
            }
        }
        // Any other status
        else {
            throw new RuntimeException("Cannot change status of " + currentStatus + " requests.");
        }
    }

    /**
     * Find matching donors and send them email notifications about the new blood
     * request.
     * Matches donors based on blood group and city.
     */
    private void notifyMatchingDonors(BloodRequest request) {
        // Find available donors matching the blood group and city
        List<DonorProfile> matchingDonors = donorRepository.findByBloodGroupAndCityAndAvailability(
                request.getBloodGroup(),
                request.getCity(),
                Availability.AVAILABLE.getValue());

        System.out.println("Found " + matchingDonors.size() + " matching donors for blood request "
                + request.getId());

        for (DonorProfile donor : matchingDonors) {
            try {
                // Get user details from userId
                User user = userRepository.findByIdAndDeletedAtIsNull(donor.getUserId()).orElse(null);
                if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
                    System.out.println("Skipping donor " + donor.getId() + " - user not found or no email");
                    continue;
                }

                // Send notification email
                sendDonorMatchNotificationEmail(user, request);
                System.out.println("Sent notification to donor: " + user.getEmail());
            } catch (Exception e) {
                System.err.println("Failed to notify donor " + donor.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Send email notification to a donor about a matching blood request
     */
    private void sendDonorMatchNotificationEmail(User donor, BloodRequest request) {
        String subject = "Blood Donation Request - " + request.getBloodGroup().getDisplayName() + " Needed";

        String htmlBody = buildDonorMatchEmailTemplate(
                donor.getName(),
                request.getBloodGroup().getDisplayName(),
                request.getCity(),
                request.getHospital(),
                request.getUnits(),
                request.getUrgency().toString());

        emailService.sendHtmlEmail(donor.getEmail(), subject, htmlBody);
    }

    /**
     * Build HTML email template for donor match notification
     */
    private String buildDonorMatchEmailTemplate(String donorName, String bloodGroup, String city,
            String hospital, int units, String urgency) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .container {
                            background-color: #f9f9f9;
                            border-radius: 10px;
                            padding: 30px;
                            border: 2px solid #e74c3c;
                        }
                        .header {
                            text-align: center;
                            color: #e74c3c;
                            margin-bottom: 20px;
                        }
                        .content {
                            background-color: white;
                            padding: 20px;
                            border-radius: 5px;
                            margin: 20px 0;
                        }
                        .details {
                            margin: 15px 0;
                        }
                        .details strong {
                            color: #e74c3c;
                        }
                        .urgency {
                            background-color: #fff3cd;
                            border-left: 4px solid #ffc107;
                            padding: 10px;
                            margin: 15px 0;
                        }
                        .footer {
                            text-align: center;
                            margin-top: 20px;
                            font-size: 12px;
                            color: #666;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1 class="header">🩸 Blood Donation Request</h1>
                        <p>Dear <strong>%s</strong>,</p>
                        <div class="content">
                            <p>A new blood donation request has been posted in your area that matches your profile!</p>
                            <div class="details">
                                <p><strong>Blood Group Required:</strong> %s</p>
                                <p><strong>City:</strong> %s</p>
                                <p><strong>Hospital:</strong> %s</p>
                                <p><strong>Units Needed:</strong> %d</p>
                            </div>
                            <div class="urgency">
                                <strong>Urgency Level:</strong> %s
                            </div>
                            <p>Your donation can save lives! If you are available and willing to donate, please respond as soon as possible.</p>
                        </div>
                        <div class="footer">
                            <p>This is an automated notification from Blood Donation System</p>
                            <p>If you are not available to donate, please update your availability status in your profile</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(donorName, bloodGroup, city, hospital, units, urgency);
    }

    private BloodRequest toEntity(BloodRequestRequestDTO dto) {
        BloodRequest entity = new BloodRequest();
        entity.setUserId(dto.getUserId());
        entity.setBloodGroup(dto.getBloodGroup());
        entity.setUnits(dto.getUnits());
        entity.setUrgency(dto.getUrgency());
        entity.setHospital(dto.getHospital());
        entity.setCity(dto.getCity());
        // Status ignored in create DTO - always set to PENDING in createRequest()
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        return entity;
    }

    private BloodRequestResponseDTO toDto(BloodRequest entity) {
        BloodRequestResponseDTO dto = new BloodRequestResponseDTO();
        dto.setId(entity.getId() != null ? entity.getId().toString() : null);
        dto.setUserId(entity.getUserId());
        dto.setBloodGroup(entity.getBloodGroup());
        dto.setUnits(entity.getUnits());
        dto.setUrgency(entity.getUrgency());
        dto.setHospital(entity.getHospital());
        dto.setCity(entity.getCity());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
