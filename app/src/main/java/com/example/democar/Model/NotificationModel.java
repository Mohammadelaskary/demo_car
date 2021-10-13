package com.example.democar.Model;

public class NotificationModel {
    private String notificationId;
    private String userId;
    private String body;
    private boolean seen;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public NotificationModel(String notificationId, String userId, String body) {
        this.body = body;
        this.notificationId = notificationId;
        this.userId = userId;
    }

    public NotificationModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
