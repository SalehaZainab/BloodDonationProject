package com.example.BloodDonationProject.repository;

import com.example.BloodDonationProject.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByNotifiableIdAndDeletedAtIsNullOrderByCreatedAtDesc(String notifiableId);

    Optional<Notification> findByIdAndDeletedAtIsNull(String id);

    List<Notification> findByNotifiableIdAndReadAtIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(String notifiableId);

    long countByNotifiableIdAndReadAtIsNullAndDeletedAtIsNull(String notifiableId);
}
