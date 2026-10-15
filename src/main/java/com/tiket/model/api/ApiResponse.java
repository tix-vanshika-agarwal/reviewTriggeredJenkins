package com.tiket.model.api;

import java.util.ArrayList;
import java.util.Map;

import static com.tiket.core.CoreFunctions.shorten;

public class ApiResponse {

    private final int statusCode;
    private final String body;
    private final Long timeTakenInSeconds;
    private final String contentType;
    private final Map<String, ArrayList<Object>> headerParams;

    private ApiResponse(Builder builder) {
        this.statusCode = builder.statusCode;
        this.body = builder.body;
        this.timeTakenInSeconds = builder.timeTakenInSeconds;
        this.contentType = builder.contentType;
        this.headerParams = builder.headerParams;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Long getTimeTakenInSeconds() {
        return timeTakenInSeconds;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, ArrayList<Object>> getHeaderParams() {
        return headerParams;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "statusCode=" + statusCode +
                ", body='" + shorten(body) + '\'' +
                ", timeTakenInSeconds=" + timeTakenInSeconds +
                ", contentType='" + contentType + '\'' +
                ", headerParams=" + headerParams +
                '}';
    }

    public static class Builder {

        private int statusCode;
        private String body;
        private Long timeTakenInSeconds;
        private String contentType;
        private Map<String, ArrayList<Object>> headerParams;

        public ApiResponse build() {
            return new ApiResponse(this);
        }

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setTimeTakenInSeconds(long timeTakenInSeconds) {
            this.timeTakenInSeconds = timeTakenInSeconds;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setHeaderParams(Map<String, ArrayList<Object>> headerParams) {
            this.headerParams = headerParams;
            return this;
        }
    }
}
