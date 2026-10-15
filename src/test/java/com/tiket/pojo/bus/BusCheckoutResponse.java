package com.tiket.pojo.bus;

public class BusCheckoutResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String status;

    public static class Data {
        public Order order;
        public ErrorData errorData;
        public int totalFare;
        public Contact contact;
    }

    public static class ErrorData {
        public Message message;
    }

    public static class Message {
        public String subTitle;
    }

    public static class Order {
        public String orderId;
        public String orderHash;
    }

    public static class Contact {
        public String profileId;
    }
}
