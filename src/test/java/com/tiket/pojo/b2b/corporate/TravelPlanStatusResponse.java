package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class TravelPlanStatusResponse {

    private boolean success;
    private String status;  // Assuming status is a string like "valid" or "invalid"
    private List<String> errors;  // If any error messages are present in the response

    // Constructor
    public TravelPlanStatusResponse(boolean success, String status, List<String> errors) {
        this.success = success;
        this.status = status;
        this.errors = errors;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
