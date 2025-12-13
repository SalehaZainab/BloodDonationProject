package com.example.BloodDonationProject.controller;

import com.example.BloodDonationProject.dto.notification.*;
import com.example.BloodDonationProject.security.RequireAuth;
import com.example.BloodDonationProject.service.NotificationService;
import com.example.BloodDonationProject.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get all notifications for the authenticated user
     */
    @GetMapping
    @RequireAuth
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            @RequestAttribute("userId") String userId) {
        List<NotificationResponse> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Notifications fetched successfully",
                notifications));
    }

    /**
     * Get unread notifications for the authenticated user
     */
    @GetMapping("/unread")
    @RequireAuth
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadNotifications(
            @RequestAttribute("userId") String userId) {
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Unread notifications fetched successfully",
                notifications));
    }

    /**
     * Get unread notification count
     */
    @GetMapping("/unread/count")
    @RequireAuth
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount(
            @RequestAttribute("userId") String userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Unread count fetched successfully",
                Map.of("count", count)));
    }

    /**
     * Mark a notification as read
     */
    @PutMapping("/{notificationId}/read")
    @RequireAuth
    public ResponseEntity<ApiResponse<Object>> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Notification marked as read"));
    }

    /**
     * Mark all notifications as read
     */
    @PutMapping("/read-all")
    @RequireAuth
    public ResponseEntity<ApiResponse<Object>> markAllAsRead(@RequestAttribute("userId") String userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "All notifications marked as read"));
    }

    /**
     * Delete a notification (soft delete)
     */
    @DeleteMapping("/{notificationId}")
    @RequireAuth
    public ResponseEntity<ApiResponse<Object>> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Notification deleted successfully"));
    }

    /**
     * Send a test notification (for testing purposes)
     */
    @PostMapping("/test")
    @RequireAuth
    public ResponseEntity<ApiResponse<Object>> sendTestNotification(
            @RequestAttribute("userId") String userId,
            @RequestBody NotificationPayload payload) {

        payload.setNotifiableId(userId);

        notificationService.sendNotificationToUser(
                userId,
                payload.getMessage(),
                payload.getRefId() != null ? payload.getRefId() : "test",
                payload.getType() != null ? payload.getType() : NotificationType.SYSTEM_NOTIFICATION.name(),
                payload.getExtra(),
                payload.getTitle(),
                payload.isPersist());

        return ResponseEntity.ok(ApiResponse.success(
                10000,
                "Test notification sent successfully"));
    }

}
