package com.tiket.pojo.accommodation.hotel;

public class SubmitBookingV2Response {
    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class CurrencyData{
        public String code;
        public int scale;
    }

    public static class Data{
        public int orderId;
        public String bookGroupId;
        public String orderHash;
        public long bookingExpiredTime;
        public CurrencyData currencyData;
        public int isInsuranceChanged;
        public int price;
        public int oldPrice;
        public Object unavailableHotels;
        public Popup popup;
    }

    public static class Popup{
        public String type;
        public String image;
        public String title;
        public String body;
        public Object action;
        public Object tracker;
    }
}
