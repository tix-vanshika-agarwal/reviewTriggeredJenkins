package com.tiket.pojo.b2b.corporate;

import java.util.ArrayList;

public class PatchTravelPolicyBodyHotelApi {
    public String productType;
    public Details details;

    public static class Details{
        public String categoryName;
        public String currency;
        public int maxPrice;
        public int minPrice;
        public ArrayList<Country> countries;
        public ArrayList<String> starRatings;
        public Hotel hotel;
    }

    public static class Hotel{
        public String policyType;
        public ArrayList<Details> details;
        public int advanceBookingDay;
        public boolean addOns;
        public boolean insurance;
    }

    public static class Country{
        public String countryCode;
        public String countryName;
    }
}
