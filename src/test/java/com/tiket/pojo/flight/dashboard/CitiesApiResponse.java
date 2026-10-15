package com.tiket.pojo.flight.dashboard;

import java.util.List;

public class CitiesApiResponse {
    public String code;
    public String message;
    public Object errors;
    public PagedData data;
    public String serverTime;

    public static class PagedData {
        public List<City> content;
        public int totalElements;
    }

    public static class City {
        public String id;
        public String code;
        public String name;
        public String countryCode;
        public Double timezone;
        public Double latitude;
        public Double longitude;
        public Double zone;
        public List<TimezoneEntry> timezones;
    }

    public static class TimezoneEntry {
        public String startDate;
        public String endDate;
        public Double timezone;
    }
}
