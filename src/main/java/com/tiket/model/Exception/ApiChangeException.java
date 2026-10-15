package com.tiket.model.Exception;

public class ApiChangeException extends RuntimeException {

    public ApiChangeException(String msg) {
        super(msg);
    }

    public ApiChangeException(String msg, Exception e) {
        super(msg, e);
    }

    public ApiChangeException(Exception e) {
        super(e);
    }

}
