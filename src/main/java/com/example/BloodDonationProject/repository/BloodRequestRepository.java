package com.example.BloodDonationProject.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.BloodRequest;
import com.example.BloodDonationProject.util.RequestStatus;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, UUID> {
	// Single filters
	List<BloodRequest> findByStatus(RequestStatus status);

	List<BloodRequest> findByCity(String city);

	List<BloodRequest> findByHospital(String hospital);

	List<BloodRequest> findByBloodGroup(BloodGroup bloodGroup);

	// Two-field combinations
	List<BloodRequest> findByStatusAndCity(RequestStatus status, String city);

	List<BloodRequest> findByStatusAndHospital(RequestStatus status, String hospital);

	List<BloodRequest> findByBloodGroupAndStatus(BloodGroup bloodGroup, RequestStatus status);

	List<BloodRequest> findByBloodGroupAndCity(BloodGroup bloodGroup, String city);

	List<BloodRequest> findByBloodGroupAndHospital(BloodGroup bloodGroup, String hospital);

	List<BloodRequest> findByCityAndHospital(String city, String hospital);

	// Three-field combinations
	List<BloodRequest> findByBloodGroupAndCityAndStatus(BloodGroup bloodGroup, String city, RequestStatus status);

	List<BloodRequest> findByBloodGroupAndHospitalAndStatus(BloodGroup bloodGroup, String hospital,
			RequestStatus status);

	List<BloodRequest> findByCityAndHospitalAndStatus(String city, String hospital, RequestStatus status);

	List<BloodRequest> findByBloodGroupAndCityAndHospital(BloodGroup bloodGroup, String city, String hospital);

	// Four-field combination (comprehensive filter)
	List<BloodRequest> findByBloodGroupAndCityAndHospitalAndStatus(BloodGroup bloodGroup, String city, String hospital,
			RequestStatus status);
}
