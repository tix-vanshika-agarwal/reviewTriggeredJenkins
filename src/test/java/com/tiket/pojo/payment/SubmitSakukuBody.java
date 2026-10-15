package com.tiket.pojo.payment;

public class SubmitSakukuBody {
    public String orderHash;
    public String paymentKey;
    public long referenceId;
    public PaymentData paymentData;

    public static class PaymentData {
        public boolean useDownPayment;
        public boolean fullSettlement;
        public String fdsToken;
    }
}
