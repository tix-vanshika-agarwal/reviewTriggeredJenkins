package com.tiket.model;

import org.apache.commons.lang3.NotImplementedException;

public enum Platform {

    IOS, ANDROID, DWEB, AMWEB, IMWEB;

    public static Platform parse(String platform) {
        switch (platform.toLowerCase()) {
            case "android" -> {
                return Platform.ANDROID;
            }
            case "ios" -> {
                return Platform.IOS;
            }
            case "dweb", "panel" -> {
                return Platform.DWEB;
            }
            case "amweb" -> {
                return Platform.AMWEB;
            }
            case "imweb" -> {
                return Platform.IMWEB;
            }
            default -> throw new NotImplementedException(String.format("Given platform: %s is not supported", platform));
        }
    }
}
