package com.tiket.pojo.b2b.corporate;

import java.util.ArrayList;

public class PatchTravelPolicyBodyFlightApi {
    public String productType;
    public Details details;
    public static class Details{
        public String categoryName;
        public String routeName;
        public ArrayList<Airline> airlines;
        public String currency;
        public int maxPrice;
        public int minPrice;
        public ArrayList<String> seatClass;
        public Flight flight;
    }

    public static class Flight{
        public String policyType;
        public ArrayList<Details> details;
        public int advanceBookingDay;
        public boolean addOns;
        public boolean insurance;
    }

    public static class Airline{
        public String airlineCode;
        public String airlineName;
    }
}
