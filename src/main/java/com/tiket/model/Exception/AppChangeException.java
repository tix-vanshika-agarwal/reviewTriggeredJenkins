package com.tiket.model.Exception;

public class AppChangeException extends RuntimeException {

    public AppChangeException(String msg) {
        super(msg);
    }

    public AppChangeException(String msg, Exception e) {
        super(msg, e);
    }

    public AppChangeException(Exception e) {
        super(e);
    }
}
