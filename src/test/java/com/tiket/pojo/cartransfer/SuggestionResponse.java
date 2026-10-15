package com.tiket.pojo.cartransfer;

import java.util.List;

public class SuggestionResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;
    public SessionData sessionData;

    public static class Data {
        public List<Suggestion> suggestions;
    }

    public static class Suggestion {
        public String locationId;
        public String name;
        public String address;
        public Pin pin;
        public String airportIataCode;
        public String countryCode;
        public String suggestionToken;
        public Object distance;
        public List<AirportBooth> airportBooth;
        public List<String> types;
        public String earliestPickupDate;
        public String earliestPickupTime;
        public String timeZoneId;
        public String iconUrl;
        public PickupDateTime pickupDateTime;
        public int minBookingTimeTransferIn;
        public int minBookingTimeTransferOut;
    }

    public static class Pin {
        public double lat;
        public double lng;
    }

    public static class AirportBooth {
        public String name;
        public String type;
        public String iconUrl;
        public String id;
    }

    public static class PickupDateTime {
        public InOut in;
        public InOut out;
    }

    public static class InOut {
        public String earliestPickupDate;
        public String earliestPickupTime;
        public int orderNowTimeOffset;
        public int timeOffset;
    }

    public static class SessionData {
        public String username;
        public String firstname;
        public String lastname;
        public String lang;
        public String currency;
        public int tixPoint;
    }
}
