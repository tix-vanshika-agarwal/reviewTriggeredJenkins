package com.tiket.pojo.b2b.corporate;

import com.google.gson.Gson;

import java.util.List;

public class TravelPlanPojo {
    public TravelPlanPojo(GetTravelPlanDetailApiResponse.Data travelPlanData) {
        this.name = travelPlanData.name;
        this.startDate = travelPlanData.startDate;
        this.endDate = travelPlanData.endDate;
        this.travelPlanID = travelPlanData.travelPlanID;
        this.travelPlanNumber = travelPlanData.travelPlanNumber;
        this.status = travelPlanData.status;
        this.customFields = travelPlanData.customFields;

        GetTravelPlanDetailApiResponse.Config config = travelPlanData.config;
        if (config.flights!=null && !config.flights.isEmpty()){
            Flight f = new Flight();
             f.arrivalCode = config.flights.get(0).arrivalCode;
             f.arrivalType = config.flights.get(0).arrivalType;
             f.departureCode = config.flights.get(0).departureCode;
             f.departureType = config.flights.get(0).departureType;
             f.isRoundTrip = config.flights.get(0).isRoundTrip;
             f.departureDate = config.flights.get(0).departureDate;
             f.returnDate = config.flights.get(0).returnDate;
             f.departureName = config.flights.get(0).departureName;
             f.arrivalName = config.flights.get(0).arrivalName;
             this.flight = f;
        }
        if(config.cars != null &&!config.cars.isEmpty()){
            Car c = new Car();
            c.pickupDate = config.cars.get(0).pickupDate;
            c.pickupTime = config.cars.get(0).pickupTime;
            c.regionalID = config.cars.get(0).regionalID;
            c.regionalName = config.cars.get(0).regionalName;
            c.withDriver = config.cars.get(0).withDriver;
            c.totalDays = config.cars.get(0).totalDays;
            this.car = c;
        }

        if(config.trains !=null && !config.trains.isEmpty()){
            Train t = new Train();
            t.departureCode = config.trains.get(0).departureCode;
            t.departureType = config.trains.get(0).departureType;
            t.departureName = config.trains.get(0).departureName;
            t.arrivalCode = config.trains.get(0).arrivalCode;
            t.arrivalType = config.trains.get(0).arrivalType;
            t.arrivalName = config.trains.get(0).arrivalName;
            t.isRoundTrip = config.trains.get(0).isRoundTrip;
            t.departureDate = config.trains.get(0).departureDate;
            t.returnDate = config.trains.get(0).returnDate;
            this.train = t;
        }

        if(config.whooshs !=null && !config.whooshs.isEmpty()){
            Whoosh w = new Whoosh();
            w.departureCode = config.whooshs.get(0).departureCode;
            w.departureType = config.whooshs.get(0).departureType;
            w.departureName = config.whooshs.get(0).departureName;
            w.arrivalCode = config.whooshs.get(0).arrivalCode;
            w.arrivalType = config.whooshs.get(0).arrivalType;
            w.arrivalName = config.whooshs.get(0).arrivalName;
            w.isRoundTrip = config.whooshs.get(0).isRoundTrip;
            w.departureDate = config.whooshs.get(0).departureDate;
            w.returnDate = config.whooshs.get(0).returnDate;
            this.whoosh = w;
        }
    }
    public int travelPlanID;
    public String travelPlanNumber;
    public String name;
    public String startDate;
    public String endDate;
    public String status;
    public Flight flight;
    public Car car;
    public Train train;
    public Whoosh whoosh;
    public List<CustomFields> customFields;

    /** Readable JSON of the travel plan (null fields are left out), e.g. for the extent report. */
    @Override
    public String toString() {
        return "TravelPlanPojo" + new Gson().toJson(this);
    }

    public static class CustomFields {
        public int id;
        public String fieldType;
        public String fieldName;
        public List<String> value;
        public FileField file;
    }

    public static class FileField {
        public String id;
        public String name;
        public String urlPath;
        public int size;
        public String signedURL;
    }

    public static class Flight {
        public String departureCode;
        public String departureType;
        public String arrivalCode;
        public String arrivalType;
        public boolean isRoundTrip;
        public String departureDate;
        public String returnDate; // Can be null
        public String departureName;
        public String arrivalName;
    }

    public static class Car {
        public String pickupDate;
        public String pickupTime;
        public String regionalID;
        public String regionalName;
        public boolean withDriver;
        public int totalDays;
    }

    public static class Train {
        public String departureCode;
        public String departureType;
        public String departureName;
        public String arrivalCode;
        public String arrivalType;
        public String arrivalName;
        public boolean isRoundTrip;
        public String departureDate;
        public String returnDate; // Can be null
    }

    public static class Whoosh {
        public String departureCode;
        public String departureType;
        public String departureName;
        public String arrivalCode;
        public String arrivalType;
        public String arrivalName;
        public boolean isRoundTrip;
        public String departureDate;
        public String returnDate; // Can be null
    }
}
