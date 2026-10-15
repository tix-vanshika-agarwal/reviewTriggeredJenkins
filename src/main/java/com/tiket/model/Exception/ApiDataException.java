package com.tiket.model.Exception;

public class ApiDataException extends RuntimeException {

    public ApiDataException(String msg) {
        super(msg);
    }

    public ApiDataException(String msg, Exception e) {
        super(msg, e);
    }

    public ApiDataException(Exception e) {
        super(e);
    }

}
