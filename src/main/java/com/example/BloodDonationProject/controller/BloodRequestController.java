package com.example.BloodDonationProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BloodDonationProject.dto.ApiResponse;
import com.example.BloodDonationProject.dto.BloodRequestRequestDTO;
import com.example.BloodDonationProject.dto.BloodRequestResponseDTO;
import com.example.BloodDonationProject.service.BloodRequestService;
import com.example.BloodDonationProject.util.RequestStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin("*")
public class BloodRequestController {
    @Autowired
    private BloodRequestService bloodRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<BloodRequestResponseDTO>> createRequest(@Valid @RequestBody BloodRequestRequestDTO dto) {
        BloodRequestResponseDTO created = bloodRequestService.createRequest(dto);
        ApiResponse<BloodRequestResponseDTO> body = ApiResponse.success("Blood request created successfully", created);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<ApiResponse<BloodRequestResponseDTO>> updateRequest(@PathVariable Long requestId, @Valid @RequestBody BloodRequestRequestDTO dto) {
        BloodRequestResponseDTO updated = bloodRequestService.updateRequest(requestId, dto);
        ApiResponse<BloodRequestResponseDTO> body = ApiResponse.success("Blood request updated successfully", updated);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponse<BloodRequestResponseDTO>> getRequestById(@PathVariable Long requestId) {
        BloodRequestResponseDTO dto = bloodRequestService.getRequestById(requestId);
        ApiResponse<BloodRequestResponseDTO> body = ApiResponse.success("Blood request fetched successfully", dto);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BloodRequestResponseDTO>>> getAllRequests(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) Integer units,
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String hospital,
            @RequestParam(required = false) Long userId) {
        
        // Start with all requests or filter by specific criteria
        List<BloodRequestResponseDTO> list;
        
        if (city != null && !city.trim().isEmpty()) {
            list = bloodRequestService.getRequestsByCity(city);
        } else if (hospital != null && !hospital.trim().isEmpty()) {
            list = bloodRequestService.getRequestsByHospital(hospital);
        } else if (bloodGroup != null && !bloodGroup.trim().isEmpty()) {
            list = bloodRequestService.getRequestsByBloodGroup(bloodGroup);
        } else {
            list = bloodRequestService.getAllRequests();
        }
        
        // Apply additional filters in memory
        if (userId != null) {
            final Long filterUserId = userId;
            list = list.stream()
                    .filter(req -> req.getUserId() != null && req.getUserId().equals(filterUserId))
                    .toList();
        }
        
        if (units != null) {
            final Integer filterUnits = units;
            list = list.stream()
                    .filter(req -> req.getUnits() == filterUnits)
                    .toList();
        }
        
        if (urgency != null && !urgency.trim().isEmpty()) {
            final String filterUrgency = urgency.trim().toUpperCase();
            list = list.stream()
                    .filter(req -> req.getUrgency() != null && req.getUrgency().toString().equalsIgnoreCase(filterUrgency))
                    .toList();
        }
        
        ApiResponse<List<BloodRequestResponseDTO>> body = ApiResponse.success("Blood requests fetched successfully", list);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<BloodRequestResponseDTO>>> getRequestsByStatus(@PathVariable RequestStatus status) {
        List<BloodRequestResponseDTO> list = bloodRequestService.getRequestsByStatus(status);
        ApiResponse<List<BloodRequestResponseDTO>> body = ApiResponse.success("Requests fetched successfully", list);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/bloodGroup/{bloodGroup}")
    public ResponseEntity<ApiResponse<List<BloodRequestResponseDTO>>> getRequestsByBloodGroup(@PathVariable String bloodGroup) {
        List<BloodRequestResponseDTO> list = bloodRequestService.getRequestsByBloodGroup(bloodGroup);
        ApiResponse<List<BloodRequestResponseDTO>> body = ApiResponse.success("Requests fetched successfully", list);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<List<BloodRequestResponseDTO>>> getRequestsByCity(@PathVariable String city) {
        List<BloodRequestResponseDTO> list = bloodRequestService.getRequestsByCity(city);
        ApiResponse<List<BloodRequestResponseDTO>> body = ApiResponse.success("Requests fetched successfully", list);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/hospital/{hospital}")
    public ResponseEntity<ApiResponse<List<BloodRequestResponseDTO>>> getRequestsByHospital(@PathVariable String hospital) {
        List<BloodRequestResponseDTO> list = bloodRequestService.getRequestsByHospital(hospital);
        ApiResponse<List<BloodRequestResponseDTO>> body = ApiResponse.success("Requests fetched successfully", list);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<ApiResponse<Void>> deleteRequest(@PathVariable Long requestId) {
        bloodRequestService.deleteRequest(requestId);
        ApiResponse<Void> body = ApiResponse.success("Blood request deleted successfully");
        return ResponseEntity.ok(body);
    }
}
