package com.tiket.pojo.accommodation.hotel;

import java.util.List;

public class UnmappedHotelsSearchResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public List<Content> content;
        public Pageable pageable;
        public boolean last;
        public int totalPages;
        public int totalElements;
        public int size;
        public int number;
        public boolean first;
        public int numberOfElements;
        public List<Sort> sort;
        public boolean empty;
    }

    public static class Content {
        public String vendorName;
        public String hotelId;
        public String hotelName;
        public String hotelAddress;
        public double longitude;
        public double latitude;
    }

    public static class Pageable {
        public int pageNumber;
        public int pageSize;
        public List<Sort> sort;
        public int offset;
        public boolean paged;
        public boolean unpaged;
    }

    public static class Sort {
        public String property;
        public String direction;
        public boolean ignoreCase;
        public String nullHandling;
        public boolean ascending;
        public boolean descending;
    }
}
