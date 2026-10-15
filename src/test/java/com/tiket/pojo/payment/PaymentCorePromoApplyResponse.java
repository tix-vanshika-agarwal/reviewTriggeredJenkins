package com.tiket.pojo.payment;

public class PaymentCorePromoApplyResponse {
    public String code;
    public Data data;
    public String message;

    public static class Data {
        public String discountType;
        public long totalDiscount;
    }
}
