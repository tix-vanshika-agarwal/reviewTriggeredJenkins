package com.tiket.pojo.accommodation.hotel;

public class SubmitBookingResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public int orderId;
        public String orderHash;
        public long bookingExpiredTime;
        public String currency;
        public Number price;
        public double oldPrice;
        public int isPriceChanged;
        public int isInsuranceChanged;
        public Object bookingAddOnResponses;
        public int orderDetailId;
    }
}