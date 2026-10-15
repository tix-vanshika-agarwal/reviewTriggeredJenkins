package com.tiket.pojo.carrental;

import java.util.List;

public class SubmitBookingV4Body {
    public String carRegionalId;
    public Contact contact;
    public String currency;
    public String driverOption;
    public boolean isApplyAllIn;
    public boolean isEvaluatePricing;
    public Passengers passengers;
    public String pickupTime;
    public int qty;
    public String startDate;
    public int totalDays;
    public int totalPrice;
    public String type;
    public String specialRequest;
    public Zones zones;
    public List<?> additionalFacilities;
    public List<?> additionalZones;

    public static class Contact {
        public String areaCode;
        public String profileId;
        public String phone;
        public String fullName;
        public String title;
        public String email;
    }

    public static class Passengers {
        public String areaCode;
        public String profileId;
        public String phone;
        public String fullName;
        public String title;
    }

    public static class DropOff {
        public String latitude;
        public String longitude;
        public String address;
        public String id;
        public String placeId;
    }

    public static class Pickup {
        public String latitude;
        public String longitude;
        public String address;
        public String id;
        public String placeId;
    }

    public static class Zones {
        public DropOff dropOff = new DropOff();
        public Pickup pickup = new Pickup();
    }
}
