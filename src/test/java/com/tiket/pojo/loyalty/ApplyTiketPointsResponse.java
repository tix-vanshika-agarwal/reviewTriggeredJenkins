package com.tiket.pojo.loyalty;

public class ApplyTiketPointsResponse {

    public String code;
    public String message;
    public Object errors;
    public String serverTime;
    public Data data;

    public static class Data {
        public int accountId;
        public int orderId;
        public int orderDetailId;
        public int lockTime;
    }
}
