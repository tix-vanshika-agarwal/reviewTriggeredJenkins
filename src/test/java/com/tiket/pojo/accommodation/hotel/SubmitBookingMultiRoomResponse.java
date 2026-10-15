package com.tiket.pojo.accommodation.hotel;

public class SubmitBookingMultiRoomResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Action {
        public Primary primary;
        public Secondary secondary;
    }

    public static class CurrencyData {
        public String code;
        public int scale;
    }

    public class Data {
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

    public class Popup {
        public String type;
        public String image;
        public String title;
        public String body;
        public Action action;
        public Tracker tracker;
    }

    public static class Primary {
        public String title;
        public String actionId;
    }

    public static class Secondary {
        public String title;
        public String actionId;
    }

    public static class Tracker {
        public String eventCategory;
        public String eventDescription;
    }
}
