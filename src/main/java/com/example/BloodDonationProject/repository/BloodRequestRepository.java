package com.example.BloodDonationProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BloodDonationProject.entity.BloodRequest;
import com.example.BloodDonationProject.util.RequestStatus;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
	// Single filters
	List<BloodRequest> findByStatus(RequestStatus status);
	List<BloodRequest> findByCity(String city);
	List<BloodRequest> findByHospital(String hospital);
	List<BloodRequest> findByBloodGroup(String bloodGroup);
	
	// Two-field combinations
	List<BloodRequest> findByStatusAndCity(RequestStatus status, String city);
	List<BloodRequest> findByStatusAndHospital(RequestStatus status, String hospital);
	List<BloodRequest> findByBloodGroupAndStatus(String bloodGroup, RequestStatus status);
	List<BloodRequest> findByBloodGroupAndCity(String bloodGroup, String city);
	List<BloodRequest> findByBloodGroupAndHospital(String bloodGroup, String hospital);
	List<BloodRequest> findByCityAndHospital(String city, String hospital);
	
	// Three-field combinations
	List<BloodRequest> findByBloodGroupAndCityAndStatus(String bloodGroup, String city, RequestStatus status);
	List<BloodRequest> findByBloodGroupAndHospitalAndStatus(String bloodGroup, String hospital, RequestStatus status);
	List<BloodRequest> findByCityAndHospitalAndStatus(String city, String hospital, RequestStatus status);
	List<BloodRequest> findByBloodGroupAndCityAndHospital(String bloodGroup, String city, String hospital);
	
	// Four-field combination (comprehensive filter)
	List<BloodRequest> findByBloodGroupAndCityAndHospitalAndStatus(String bloodGroup, String city, String hospital, RequestStatus status);
}
