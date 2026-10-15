package com.tiket.model.Exception;

public class DriverToolsException extends RuntimeException {

    public DriverToolsException(String msg) {
        super(msg);
    }

    public DriverToolsException(String msg, Exception e) {
        super(msg, e);
    }

    public DriverToolsException(Exception e) {
        super(e);
    }
}
