package com.tiket.pojo.loyalty;

import java.util.List;

public class PendingReviewResponse {
    public String code;
    public String message;
    public Object errors;
    public String serverTime;
    public Data data;

    public static class Data{
        public List<Content> content;
    }

    public static class Content{
        public String token;
        public String inventoryId;
        public String orderId;
    }
}
