package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class GetTravelPlanSearchApiResponse {
    public boolean success;
    public List<DataItem> data;
    public Metadata metadata;

    public static class DataItem {
        public int travelPlanID;
        public String travelPlanNumber;
        public String name;
        public List<Product> products;
        public String startDate;
        public String endDate;
        public String status;
        public String requestedAt;
        public String requestorName;
        public String travelPolicyID;
        public String travelPolicyName;
        public String notes;
    }

    public static class Product {
        public boolean isRoundTrip;
        public String productType;
        public boolean isWithDriver;
        public String paymentStatus;
    }

    public static class Metadata {
        public int page;
        public int size;
        public int count;
        public int totalCount;
        public ExtInfo extInfo;
    }

    public static class ExtInfo {
        public int pendingRequestCount;
    }
}
