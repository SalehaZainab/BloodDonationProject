package com.example.BloodDonationProject.repository;

import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    boolean existsByEmailAndDeletedAtIsNull(String email);

    Optional<User> findByIdAndDeletedAtIsNull(String id);

    List<User> findByCityAndDeletedAtIsNull(String city);

    List<User> findByBloodGroupAndDeletedAtIsNull(BloodGroup bloodGroup);

    List<User> findByCityAndBloodGroupAndDeletedAtIsNull(String city, BloodGroup bloodGroup);

    List<User> findByIsActiveAndDeletedAtIsNull(Boolean isActive);

    List<User> findAllByDeletedAtIsNull();
}
