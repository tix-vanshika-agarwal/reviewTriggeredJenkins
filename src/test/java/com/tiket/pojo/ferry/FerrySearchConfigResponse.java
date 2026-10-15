package com.tiket.pojo.ferry;

import java.util.List;

public class FerrySearchConfigResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public List<Port> ports;
        public List<Route> routes;
        public CheckInTime checkInTime;
        public int maxSearchInDays;
        public List<VehicleType> vehicleTypes;
        public Passengers passengers;
        public Object errorData;
    }

    public static class Port {
        public String portId;
        public String name;
        public String address;
    }

    public static class Route {
        public String origin;
        public String destination;
        public boolean isDefaultPair;
    }

    public static class CheckInTime {
        public int minimumInMinute;
        public int intervalInMinute;
        public String description;
    }

    public static class VehicleType {
        public String id;
        public String name;
        public String subtitle;
        public String type;
        public String imageUrl;
        public String description;
        public int maxCapacity;
        public boolean isVehicle;
    }

    public static class Passengers {
        public String description;
        public String adultDescription;
        public String childDescription;
        public String infantDescription;
        public String elderlyDescription;
    }
}

