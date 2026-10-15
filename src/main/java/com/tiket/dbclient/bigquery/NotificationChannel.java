package com.tiket.dbclient.bigquery;

import java.util.List;

public enum NotificationChannel {

    EMAIL(List.of("send_sendgrid", "send_mailgun")),
    WHATSAPP(List.of("send_whatsapp"));

    private final List<String> activityTypes;

    NotificationChannel(List<String> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public List<String> getActivityTypes() {
        return activityTypes;
    }
}
