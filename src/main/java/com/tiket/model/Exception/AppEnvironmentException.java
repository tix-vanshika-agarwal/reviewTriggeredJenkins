package com.tiket.model.Exception;

public class AppEnvironmentException extends RuntimeException {

    public AppEnvironmentException(String msg) {
        super(msg);
    }

    public AppEnvironmentException(String msg, Exception e) {
        super(msg, e);
    }

    public AppEnvironmentException(Exception e) {
        super(e);
    }
}
