package com.tiket.pojo.loyalty;

public class ReviewOrderSaveResponse {
    public String code;
    public String message;
    public Object errors;
    public Object serverTime;
    public Data data;

    public static class Data{
        public String accountId;
        public String inventoryId;
        public String orderId;
        public String productType;
    }
}
