package com.example.BloodDonationProject.service;

import com.example.BloodDonationProject.dto.notification.*;
import com.example.BloodDonationProject.entity.Notification;
import com.example.BloodDonationProject.entity.User;
import com.example.BloodDonationProject.entity.UserRole;
import com.example.BloodDonationProject.repository.NotificationRepository;
import com.example.BloodDonationProject.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.name:Blood Donation App}")
    private String appName;

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    /**
     * Send notification using Firebase Cloud Messaging
     */
    public String sendNotification(String userId, String body, Map<String, Object> payload) {
        try {
            if (!firebaseEnabled) {
                System.out.println("Firebase is disabled. Skipping notification send.");
                return "Firebase disabled";
            }

            String title = payload.get("title") != null ? payload.get("title").toString() : appName;

            // Build data map
            Map<String, String> data = new HashMap<>();
            data.put("title", title);
            data.put("body", body);
            data.put("payload", objectMapper.writeValueAsString(payload));
            data.put("sound", "default");
            data.put("click_action", "FLUTTER_NOTIFICATION_CLICK");

            // Build FCM message
            Message message = Message.builder()
                    .setTopic(userId)
                    .setNotification(com.google.firebase.messaging.Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setSound("ringtone1.aiff")
                                    .setClickAction("FLUTTER_NOTIFICATION_CLICK")
                                    .build())
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setAlert(ApsAlert.builder()
                                            .setTitle(appName)
                                            .setBody(body)
                                            .build())
                                    .setSound("ringtone1.aiff")
                                    .build())
                            .putCustomData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                            .build())
                    .build();

            System.out.println("Sending FCM message to topic: " + userId);
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("FCM response: " + response);
            return response;

        } catch (Exception e) {
            System.err.println("FCM Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error sending notification: " + e.getMessage(), e);
        }
    }

    /**
     * Send notification to user with persistence
     */
    @Transactional
    public void sendNotificationToUser(String userId, String body, Object referenceId,
            String notificationType, Map<String, Object> extra, String title, boolean persist) {
        try {
            System.out.println("Sending notification to user: " + userId);

            // Check if user exists and is not an admin with push notifications enabled
            Optional<User> userOpt = userRepository.findByIdAndDeletedAtIsNull(userId);
            if (userOpt.isEmpty()) {
                System.out.println("User with id " + userId
                        + " not found to send notification because either the push notification was off or user doesn't exist");
                return;
            }

            User user = userOpt.get();
            if (user.getRole() == UserRole.ADMIN) {
                System.out.println("User with id " + userId
                        + " not found to send notification because either the push notification was off or he was Admin");
                return;
            }

            // Build payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("notifiableId", userId);
            payload.put("refId", referenceId);
            payload.put("title", title != null ? title : appName);
            payload.put("message", body);
            payload.put("type", notificationType);
            if (extra != null) {
                payload.put("extra", extra);
            }
            payload.put("createdAt", LocalDateTime.now().toString());

            // Persist to database if needed
            if (persist) {
                Notification notification = new Notification();
                notification.setNotifiableId(userId);
                notification.setRefId(referenceId != null ? referenceId.toString() : null);
                notification.setTitle(title != null ? title : appName);
                notification.setMessage(body);
                notification.setType(notificationType);
                notification.setExtra(extra != null ? objectMapper.writeValueAsString(extra) : null);
                notificationRepository.save(notification);
            }

            // Send notification via Firebase
            sendNotification(userId, body, payload);

        } catch (Exception e) {
            System.err.println("Error sending notification to user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Get notifications for a user
     */
    public List<NotificationResponse> getNotifications(String userId) {
        List<Notification> notifications = notificationRepository
                .findByNotifiableIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Get unread notifications for a user
     */
    public List<NotificationResponse> getUnreadNotifications(String userId) {
        List<Notification> notifications = notificationRepository
                .findByNotifiableIdAndReadAtIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Mark notification as read
     */
    @Transactional
    public void markAsRead(String notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findByIdAndDeletedAtIsNull(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    /**
     * Mark all notifications as read for a user
     */
    @Transactional
    public void markAllAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository
                .findByNotifiableIdAndReadAtIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(userId);

        LocalDateTime now = LocalDateTime.now();
        unreadNotifications.forEach(notification -> notification.setReadAt(now));
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Delete notification (soft delete)
     */
    @Transactional
    public void deleteNotification(String notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findByIdAndDeletedAtIsNull(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setDeletedAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    /**
     * Get unread notification count
     */
    public long getUnreadCount(String userId) {
        return notificationRepository.countByNotifiableIdAndReadAtIsNullAndDeletedAtIsNull(userId);
    }

    // Helper methods
    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setNotifiableId(notification.getNotifiableId());
        response.setRefId(notification.getRefId());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType());
        response.setCreatedAt(notification.getCreatedAt());
        response.setReadAt(notification.getReadAt());

        // Parse extra JSON string to Map
        if (notification.getExtra() != null && !notification.getExtra().isEmpty()) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> extraMap = objectMapper.readValue(notification.getExtra(), Map.class);
                response.setExtra(extraMap);
            } catch (Exception e) {
                System.err.println("Error parsing extra data: " + e.getMessage());
            }
        }

        return response;
    }

    private Map<String, String> convertToStringMap(Map<String, Object> map) {
        Map<String, String> stringMap = new HashMap<>();
        map.forEach((key, value) -> stringMap.put(key, value != null ? value.toString() : ""));
        return stringMap;
    }
}
