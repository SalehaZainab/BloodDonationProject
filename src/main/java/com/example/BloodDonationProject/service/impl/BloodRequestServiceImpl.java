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
import com.example.BloodDonationProject.repository.BloodRequestRepository;
import com.example.BloodDonationProject.service.BloodRequestService;
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
