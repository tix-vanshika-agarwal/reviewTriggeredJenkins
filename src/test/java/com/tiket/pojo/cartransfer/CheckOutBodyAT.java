package com.tiket.pojo.cartransfer;

import java.util.List;

public class CheckOutBodyAT {
    public String cartId;
    public ContactPerson contactPerson;
    public List<Detail> details;
    public List<Passenger> passengers;
    public String pickupNotes;
    public int totalUnit;
    public List<Object> facilities;
    public String specialRequest;
    public FlightInfo flightInfo;

    public static class ContactPerson {
        public String title;
        public long profileId;
        public long accountId;
        public String email;
        public String firstName;
        public String lastName;
        public String phoneCode;
        public String phoneNumber;
    }

    public static class Detail {
        public String name;
        public String value;
    }

    public static class Passenger {
        public String title;
        public long profileId;
        public String firstName;
        public String lastName;
        public String phoneCode;
        public String phoneNumber;
    }

    public static class FlightInfo {
        public String flightNumber;
        public String airline;
        public String origin;
        public String destination;
    }
}
