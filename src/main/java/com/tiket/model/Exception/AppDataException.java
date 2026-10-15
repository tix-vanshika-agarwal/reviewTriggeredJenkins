package com.tiket.model.Exception;

public class AppDataException extends RuntimeException {

    public AppDataException(String msg) {
        super(msg);
    }

    public AppDataException(String msg, Exception e) {
        super(msg, e);
    }

    public AppDataException(Exception e) {
        super(e);
    }
}
