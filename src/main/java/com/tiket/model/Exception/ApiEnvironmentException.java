package com.tiket.model.Exception;

public class ApiEnvironmentException extends RuntimeException {

    public ApiEnvironmentException(String msg) {
        super(msg);
    }

    public ApiEnvironmentException(String msg, Exception e) {
        super(msg, e);
    }

    public ApiEnvironmentException(Exception e) {
        super(e);
    }
}
