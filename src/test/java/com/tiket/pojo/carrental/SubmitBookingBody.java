package com.tiket.pojo.carrental;

public class SubmitBookingBody {
    public String carRegionalId;
    public Contact contact;
    public String currency;
    public String driverOption;
    public boolean isApplyAllIn;
    public Passengers passengers;
    public String pickupTime;
    public String qty;
    public String startDate;
    public int totalDays;
    public String type;
    public Zones zones;
    public Object isEvaluatePickupDropoff;
    public Object additionalZones;

    public static class Contact {
        public String areaCode;
        public String phone;
        public String fullName;
        public String title;
        public String email;
    }

    public static class Passengers {
        public String areaCode;
        public String phone;
        public String fullName;
        public String title;
        public String email;
    }

    public static class DropOff {
        public String address;
        public String id;
    }

    public static class Pickup {
        public String address;
        public String id;
    }

    public static class Zones {
        public DropOff dropOff = new DropOff();
        public Pickup pickup = new Pickup();
    }
}