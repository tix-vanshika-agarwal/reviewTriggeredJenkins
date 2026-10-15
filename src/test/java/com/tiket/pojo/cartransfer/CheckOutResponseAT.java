package com.tiket.pojo.cartransfer;

public class CheckOutResponseAT {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data{
        public long orderID;
        public String localOrderId;
        public String orderHash;
        public String paymentUrl;
        public String bookingStatus;
        public String cartId;
    }
}
