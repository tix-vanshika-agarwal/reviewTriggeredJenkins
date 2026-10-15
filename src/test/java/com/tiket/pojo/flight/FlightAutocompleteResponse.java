package com.tiket.pojo.flight;

import java.util.ArrayList;

public class FlightAutocompleteResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Airport{
        public String icon;
        public String cityCode;
        public String airportCode;
        public String countryCode;
        public String airportName;
        public String cityName;
        public String countryName;
        public double timezone;
        public String type;
        public ArrayList<Child> child;
    }

    public static class Child{
        public String icon;
        public String cityCode;
        public String airportCode;
        public String countryCode;
        public String airportName;
        public String cityName;
        public String countryName;
        public double timezone;
        public String type;
        public ArrayList<Object> child;
    }

    public static class Data{
        public ArrayList<Airport> airports;
    }
}
