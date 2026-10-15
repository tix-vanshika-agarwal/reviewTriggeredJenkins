package com.tiket.model.Exception;

public class ApiToolsException extends RuntimeException {

    public ApiToolsException(String msg) {
        super(msg);
    }

    public ApiToolsException(String msg, Exception e) {
        super(msg, e);
    }

    public ApiToolsException(Exception e) {
        super(e);
    }
}
