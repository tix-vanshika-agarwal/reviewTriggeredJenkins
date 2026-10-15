package com.tiket.pojo.discount;

public class DiscountResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data {
        public String orderId;
        public String orderHash;
        public String productType;
        public String discountCode;
        public String discountType;
        public Double totalPrice;
        public String currency;
        public Double totalDiscount;
        public String orderDetailId;
    }
}
