package com.tiket.pojo.payment;

public class SubmitCreditCardBody {
    public String orderHash;
    public long referenceId;
    public String paymentKey;
    public String subPaymentMethod;
    public PaymentData paymentData;

    public static class PaymentData {
        public CreditCardRequest creditCardRequest;
        public boolean useDownPayment;
        public boolean fullSettlement;
        public String fdsToken;
    }

    public static class CreditCardRequest {
        public String cardNumber;
        public String cardHolderName;
        public String cardExpiredMonth;
        public String cardExpiredYear;
        public String cardCvvNumber;
        public int installmentMonth;
        public String saveCc;
        public String sessionId;
        public String kkiOneTimeToken;
        public String billingAddress;
        public String postalCode;
    }
}
