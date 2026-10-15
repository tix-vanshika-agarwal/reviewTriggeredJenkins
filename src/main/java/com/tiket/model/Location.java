package com.tiket.model;

import org.apache.commons.lang3.NotImplementedException;

public enum Location {

    LOCAL, GOOGLE;

    public static Location parse(String location) {
        switch (location.toLowerCase()) {
            case "local" -> {
                return LOCAL;
            }
            case "google" -> {
                return GOOGLE;
            }
            default -> throw new NotImplementedException(String.format("Given location: %s is not supported", location));
        }
    }
}
