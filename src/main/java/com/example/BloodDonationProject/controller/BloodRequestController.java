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
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<BloodRequestResponseDTO> createRequest(@Valid @RequestBody BloodRequestRequestDTO dto) {
        BloodRequestResponseDTO created = bloodRequestService.createRequest(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<BloodRequestResponseDTO> updateRequest(@PathVariable Long requestId, @Valid @RequestBody BloodRequestRequestDTO dto) {
        BloodRequestResponseDTO updated = bloodRequestService.updateRequest(requestId, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<BloodRequestResponseDTO> getRequestById(@PathVariable Long requestId) {
        BloodRequestResponseDTO dto = bloodRequestService.getRequestById(requestId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<BloodRequestResponseDTO>> getAllRequests() {
        List<BloodRequestResponseDTO> list = bloodRequestService.getAllRequests();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BloodRequestResponseDTO>> getRequestsByStatus(@PathVariable RequestStatus status) {
        List<BloodRequestResponseDTO> list = bloodRequestService.getRequestsByStatus(status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/bloodGroup/{bloodGroup}")
    public ResponseEntity<List<BloodRequestResponseDTO>> getRequestsByBloodGroup(@PathVariable String bloodGroup) {
        List<BloodRequestResponseDTO> list = bloodRequestService.getRequestsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<BloodRequestResponseDTO>> getRequestsByCity(@PathVariable String city) {
        List<BloodRequestResponseDTO> list = bloodRequestService.getRequestsByCity(city);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/hospital/{hospital}")
    public ResponseEntity<List<BloodRequestResponseDTO>> getRequestsByHospital(@PathVariable String hospital) {
        List<BloodRequestResponseDTO> list = bloodRequestService.getRequestsByHospital(hospital);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long requestId) {
        bloodRequestService.deleteRequest(requestId);
        return ResponseEntity.noContent().build();
    }
}
