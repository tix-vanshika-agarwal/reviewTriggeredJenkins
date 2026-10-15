package com.tiket.pojo.b2b.corporate;

import java.util.ArrayList;
import java.util.List;

/**
 * Create travel plan body for a multi-city flight plan ({@code "tripType":"MULTI_CITY"}).
 * Same as {@link PostCreateTravelPlanBody}, but each flight holds its routes under {@code segments[]}
 * instead of a single route on the flight itself.
 */
public class PostCreateTravelPlanMultiCityBody {
    public String name;
    public String startDate;
    public String endDate;
    public Configs configs;
    public List<Integer> approverIDs;
    public ArrayList<PostCreateTravelPlanBody.customFields> customFields; // default empty

    public static class Configs {
        public List<Flight> flights;
    }

    public static class Flight {
        public String tripType = "MULTI_CITY";
        public List<Segment> segments;
    }

    public static class Segment {
        public String departureCode;
        public String departureType;
        public String departureName;
        public String arrivalCode;
        public String arrivalType;
        public String arrivalName;
        public String departureDate;
    }
}
