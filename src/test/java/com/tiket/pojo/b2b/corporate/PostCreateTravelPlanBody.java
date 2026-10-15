package com.tiket.pojo.b2b.corporate;

import java.util.ArrayList;
import java.util.List;

public class PostCreateTravelPlanBody {
    public String name;
    public String startDate;
    public String endDate;
    public Configs configs;
    public List<Integer> approverIDs;
    public ArrayList<customFields> customFields; // default empty

    public static class customFields {
        public int id;
        public String fieldType;
        public String fieldName;
        public List<String> value;
        public File file;
    }

    public static class File {
        public String id;
        public String name;
        public Integer size;
        public String fileURL;
    }

    public static class Configs {
        public List<Hotel> hotels;
        public List<Flight> flights;
        public List<Car> cars;
        public List<Train> trains;
        public List<Whoosh> whooshs;
    }

    public static class Hotel {
        public String publicId;
        public String location;
        public String startDate;
        public String endDate;
        public int nights;
        public String locationType;
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
