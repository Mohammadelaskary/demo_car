package com.example.democar.Notification;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("to")
    String token;
    Notification notification;

    public Message(String token, Notification notification) {
        this.token = token;
        this.notification = notification;
    }

    public Message() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
