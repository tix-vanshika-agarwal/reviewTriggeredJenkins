package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class PostCreateTravelPolicyApiBodyAll {
    public String name;
    public Assignees assignees;
    public Details details;

    public static class Assignees {
        public String source;
        public List<Object> excludedEmployeeIDs;
        public List<Integer> employeeIDs;
        public List<Object> departmentIDs;
        public List<Object> groupIDs;
        public boolean allEmployees;
    }

    public static class Details {
        public Flight flight;
        public Hotel hotel;
        public AirportTransfer airportTransfer;
        public Train train;
        public Bus bus;
        public Ferry ferry;
        public Car car;
        public Event event;
    }

    public static class Flight {
        public String policyType;
        public int advanceBookingDay;
        public boolean addOns;
        public boolean insurance;
        public List<FlightDetail> details;
    }

    public static class FlightDetail {
        public List<Airline> airlines;
        public String currency;
        public int maxPrice;
        public int minPrice;
        public List<String> seatClass;
        public String routeName;
        public String categoryName;
    }

    public static class Airline {
        public String airlineCode;
        public String airlineName;
    }

    public static class Hotel {
        public String policyType;
        public int advanceBookingDay;
        public boolean addOns;
        public boolean insurance;
        public List<HotelDetail> details;
    }

    public static class HotelDetail {
        public String currency;
        public int maxPrice;
        public int minPrice;
        public List<String> starRatings;
        public String categoryName;
        public List<Object> countries;
    }

    public static class AirportTransfer {
        public boolean enableBooking;
    }

    public static class Train {
        public boolean enableBooking;
    }

    public static class Bus {
        public boolean enableBooking;
    }

    public static class Ferry {
        public boolean enableBooking;
    }

    public static class Car {
        public boolean enableBooking;
    }

    public static class Event {
        public boolean enableBooking;
    }
}
