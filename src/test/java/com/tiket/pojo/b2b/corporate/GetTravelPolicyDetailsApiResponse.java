package com.tiket.pojo.b2b.corporate;

import java.util.ArrayList;

public class GetTravelPolicyDetailsApiResponse {
    public boolean success;
    public Data data;

    public static class Data{
        public int id;
        public int corporateID;
        public String name;
        public ArrayList<Detail> details;
    }

    public static class Detail{
        public int id;
        public int travelPolicyID;
        public String productType;
        public Config config;
        public ArrayList<Object> airlines;
        public String currency;
        public int duration;
        public int maxPrice;
        public int minPrice;
        public String routeName;
        public ArrayList<Object> seatClass;
        public String categoryName;
        public ArrayList<Object> countries;
        public ArrayList<String> starRatings;
    }

    public class Config{
        public boolean addOns;
        public ArrayList<Detail> details;
        public boolean insurance;
        public String policyType;
        public int advanceBookingDay;
        public boolean enableBooking;
    }
}
