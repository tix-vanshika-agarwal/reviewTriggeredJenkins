package com.tiket.pojo.carrental;

public class SubmitBookingResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public String orderId;
        public String orderHash;
    }
}