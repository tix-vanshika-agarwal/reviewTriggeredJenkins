package com.tiket.pojo.flight.dashboard;

import java.util.List;

public class AirportByCityCodeResponse {
    public String code;
    public String message;
    public Object errors;
    public PagedData data;
    public String serverTime;

    public static class PagedData {
        public List<Airport> content;
        public int totalElements;
    }

    public static class Airport {
        public String id;
        public String code;
        public String cityCode;
        public String countryCode;
        public String status;
    }
}
