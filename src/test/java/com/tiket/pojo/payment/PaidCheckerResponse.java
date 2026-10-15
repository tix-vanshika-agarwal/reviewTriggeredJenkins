package com.tiket.pojo.payment;

public class PaidCheckerResponse {
    
    public Data data;

    public static class Data {
        public boolean paid;
        public String paymentStatus;
    }
}
