package com.example.democar.Notification;

public class RootModel {
    String to;
    Notification notification;

    public RootModel(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public RootModel() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
