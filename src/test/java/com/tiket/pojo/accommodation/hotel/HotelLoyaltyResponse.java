package com.tiket.pojo.accommodation.hotel;

import java.util.List;

public class HotelLoyaltyResponse {
    public String code;
    public String message;
    public Data data;

    public static class Data {
        public String id;
        public int version;
        public int hotelId;
        public double basePercentWorth;
        public List<ExtraBenefits> extraBenefits;
        public List<RoomTypes> roomTypes;
        public List<String> blackoutDates;
        public boolean active;

        public static class ExtraBenefits {
            public String id;
            public int version;
            public String code;
            public String title;
            public String subTitle;
            public String titleExtranet;
            public String subTitleExtranet;
            public boolean isActive;
            public int priority;
        }
        public static class RoomTypes {
            public String id;
            public int version;
            public String name;
        }
    }
}
