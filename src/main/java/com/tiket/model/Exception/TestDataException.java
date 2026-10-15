package com.tiket.model.Exception;

public class TestDataException extends RuntimeException {

    public TestDataException(String msg) {
        super(msg);
    }

    public TestDataException(String msg, Exception e) {
        super(msg, e);
    }

    public TestDataException(Exception e) {
        super(e);
    }
}
