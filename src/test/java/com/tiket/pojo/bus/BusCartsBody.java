package com.tiket.pojo.bus;

public class BusCartsBody {

    public Schedules schedules;
    public PassengerCount passengerCount;

    public static class Schedules {
        public Depart depart;
    }

    public static class Depart {
        public String id;
        public String hash;
    }

    public static class PassengerCount {
        public int adult;
    }
}
