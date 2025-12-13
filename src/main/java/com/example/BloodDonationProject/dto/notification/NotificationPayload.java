package com.example.BloodDonationProject.dto.notification;

import java.util.Map;

public class NotificationPayload {

    private String notifiableId;
    private String refId;
    private String title;
    private String message;
    private String type;
    private Map<String, Object> extra;
    private boolean persist = true;

    // Constructors
    public NotificationPayload() {
    }

    public NotificationPayload(String notifiableId, String title, String message, String type) {
        this.notifiableId = notifiableId;
        this.title = title;
        this.message = message;
        this.type = type;
    }

    // Getters and Setters
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

    public boolean isPersist() {
        return persist;
    }

    public void setPersist(boolean persist) {
        this.persist = persist;
    }
}
