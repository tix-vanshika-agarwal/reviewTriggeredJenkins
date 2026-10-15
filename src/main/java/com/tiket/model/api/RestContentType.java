package com.tiket.model.api;

public enum RestContentType {

    JSON("application/json"),
    TEXT("text/plain"),
    FORM_DATA("multipart/form-data"),
    FORM_URL_ENCODED("application/x-www-form-urlencoded");

    private final String contentType;

    RestContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
