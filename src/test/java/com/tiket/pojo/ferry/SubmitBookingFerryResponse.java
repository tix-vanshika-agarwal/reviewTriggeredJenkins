package com.tiket.pojo.ferry;

public class SubmitBookingFerryResponse {
    public String code;
    public String message;
    Object errors;
    public Data data;

    public static class Data{
        public String cartId;
        public String secret;
        Object expiryTime;
        public Order order;
    }

    public static class Order{
        public String orderId;
        public String orderHash;
        public String paymentExpired;
        public String paymentUrl;
        public String status;
    }
}
