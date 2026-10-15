package com.tiket.pojo.payment;

import java.util.List;

public class PaymentCorePromoSuggestionResponse {
    public String code;
    public String message;
    public List<Promo> data;

    public static class Promo {
        public String code;
        public String description;
        public String source;
        public boolean eligible;
        public boolean needChangePaymentMethod;
        public double amount;
        public double cashback;
        public double totalDiscount;
        public List<String> paymentMethods;

        public boolean isPayableWithSakuku() {
            return paymentMethods != null
                    && (paymentMethods.contains("ALL_PAYMENT_METHODS") || paymentMethods.contains("SAKUKU"));
        }
    }
}
