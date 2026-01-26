package com.example.dogshare.Objects;

public class AppNotification {
    private String notificationId;
    private String userId;

    private String type; // walkReminder / feedReminder / meetingInvite
    private String title;
    private String message;
    private long time;

    private boolean isRead;
}
