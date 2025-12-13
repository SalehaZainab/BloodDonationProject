package com.example.BloodDonationProject.service;

import java.util.List;

import com.example.BloodDonationProject.dto.BloodRequestRequestDTO;
import com.example.BloodDonationProject.dto.BloodRequestResponseDTO;
import com.example.BloodDonationProject.util.RequestStatus;

public interface BloodRequestService {
    BloodRequestResponseDTO createRequest(BloodRequestRequestDTO dto);

    BloodRequestResponseDTO updateRequest(String requestId, BloodRequestRequestDTO dto);

    BloodRequestResponseDTO getRequestById(String requestId);

    List<BloodRequestResponseDTO> getAllRequests();

    List<BloodRequestResponseDTO> getRequestsByStatus(RequestStatus status);

    List<BloodRequestResponseDTO> getRequestsByBloodGroup(String bloodGroup);

    List<BloodRequestResponseDTO> getRequestsByCity(String city);

    List<BloodRequestResponseDTO> getRequestsByHospital(String hospital);

    void deleteRequest(String requestId);
}
