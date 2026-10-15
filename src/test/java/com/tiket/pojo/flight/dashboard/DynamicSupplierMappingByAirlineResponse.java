package com.tiket.pojo.flight.dashboard;

import java.util.List;

public class DynamicSupplierMappingByAirlineResponse {
    public String code;
    public String message;
    public Object errors;
    public PagedData data;
    public long serverTime;

    public static class PagedData {
        public List<SupplierMapping> content;
        public Object pageable;
        public int totalPages;
        public boolean last;
        public int totalElements;
        public boolean first;
        public Object sort;
        public int number;
        public int numberOfElements;
        public int size;
        public boolean empty;
    }

    public static class SupplierMapping {
        public String id;
        public String origin;
        public String destination;
        public String airline;
        public String status;
        public Supplier supplier;
        public int priority;
        public String updatedDate;
        public boolean verified;
        public String updatedBy;
    }

    public static class Supplier {
        public String name;
        public String distributionSystem;
        public String code;
    }
}
