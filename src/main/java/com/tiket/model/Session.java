package com.tiket.model;

import org.openqa.selenium.WebDriver;

import java.util.Objects;

public record Session<T extends WebDriver>(
        String sessionId,
        T driver,
        long createdAt,
        int timeLimitMinutes
) {
    public Session {
        Objects.requireNonNull(sessionId);
        Objects.requireNonNull(driver);
    }
}
