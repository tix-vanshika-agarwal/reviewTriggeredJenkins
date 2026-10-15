package com.tiket.pojo.bus;

public class BusCartsResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data {
        public String cartId;
        public String secret;
        public PriceDetail priceDetail;
    }

    public static class PriceDetail {
        public int totalPrice;
    }
}
