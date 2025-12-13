package com.example.BloodDonationProject.dto.notification;

import java.time.LocalDateTime;
import java.util.Map;

public class NotificationResponse {

    private String id;
    private String notifiableId;
    private String refId;
    private String title;
    private String message;
    private String type;
    private Map<String, Object> extra;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    // Constructors
    public NotificationResponse() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotifiableId() {
        return notifiableId;
    }

    public void setNotifiableId(String notifiableId) {
        this.notifiableId = notifiableId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
