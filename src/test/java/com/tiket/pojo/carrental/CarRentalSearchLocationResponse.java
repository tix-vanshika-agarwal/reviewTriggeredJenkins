package com.tiket.pojo.carrental;

public class CarRentalSearchLocationResponse {
    public String code;
    public String message;
    public String errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public String placeId;
        public String name;
        public String address;
        public String latitude;
        public String longitude;
    }
}