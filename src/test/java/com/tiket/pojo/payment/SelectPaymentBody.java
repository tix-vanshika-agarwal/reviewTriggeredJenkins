package com.tiket.pojo.payment;

public class SelectPaymentBody {

    public String operationName;
    public Variables variables;
    public String query;

    public static class Variables {
        public long referenceId;
        public String orderHash;
        public String paymentKey;
        public String paymentType;
        public long rescheduleOrderId;
        public String rescheduleOrderHash;
    }
}
