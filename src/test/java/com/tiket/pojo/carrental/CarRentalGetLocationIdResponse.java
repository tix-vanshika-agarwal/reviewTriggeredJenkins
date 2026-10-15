package com.tiket.pojo.carrental;

public class CarRentalGetLocationIdResponse {
    public String code;
    public String message;
    public String errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public String placeId;
        public String zoneType;
        public String additionalZoneInfo;
        public String mainText;
        public String secondaryText;
        public String regionalId;
        public String regionalName;
        public Zone zone;
        public String latitude;
        public String longitude;
        public String pickedLocationMessage;
    }

    public static class Zone {
        public String name;
        public String id;
        public long price;
    }
}