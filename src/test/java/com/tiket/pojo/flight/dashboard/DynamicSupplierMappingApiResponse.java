package com.tiket.pojo.flight.dashboard;

import java.util.List;

public class DynamicSupplierMappingApiResponse {
    public String code;
    public String message;
    public Object errors;
    public List<Data> data;
    public long serverTime;

    public static class Data {
        public String id;
        public String origin;
        public String destination;
        public String airline;
        public String status;
        public Supplier supplier;
        public int priority;
        public String updatedDate;
        public MetaData metaData;
        public boolean verified;
        public String updatedBy;
    }

    public static class Supplier {
        public String name;
        public String distributionSystem;
        public String code;
    }

    public static class MetaData {
        public boolean isTopSupplier;
    }
}
