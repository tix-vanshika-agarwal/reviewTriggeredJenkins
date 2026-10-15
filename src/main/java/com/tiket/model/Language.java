package com.tiket.model;

import org.apache.commons.lang3.NotImplementedException;

public enum Language {
    ENGLISH, INDONESIAN;

    public static Language parse(String language) {
        switch (language.toLowerCase()) {
            case "english" -> {
                return ENGLISH;
            }
            case "bahasa_indonesia" -> {
                return INDONESIAN;
            }
            default -> throw new NotImplementedException("Given language: %s is not supported", language);
        }
    }
}
