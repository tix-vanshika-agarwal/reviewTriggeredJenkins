package com.tiket.pojo.orderDweb;

import java.util.List;

public class LandingPageResponse {
    public String code;
    public String message;
    Object errors;
    public Data data;

    public static class Data {
        public EntryPayment entryPayment;
    }

    public static class EntryPayment {
        public List<Recommendation> recommendations;
    }

    public static class Recommendation {
        public String paymentMethod;
    }
}
