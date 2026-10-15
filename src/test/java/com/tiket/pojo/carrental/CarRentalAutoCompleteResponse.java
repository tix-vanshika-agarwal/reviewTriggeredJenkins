package com.tiket.pojo.carrental;
import java.util.List;

public class CarRentalAutoCompleteResponse {
    public String code;
    public String message;
    public String errors;
    public List<Data> data;
    public String serverTime;

    public static class Data{
        public String placeId;
        public String mainText;
        public String secondaryText;
        public String iconUrl;
        public String regionalId;
        public String regionalName;
        public String levelId;
        public String levelName;
        public String minPickupTime;
        public String maxPickupTime;
        public String timeZone;
        public int pickupOffsetMinute;
        public String provinceId;
        public String provinceName;
        public String cityId;
        public String cityName;
    }
}