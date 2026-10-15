package com.tiket.pojo.paymentDweb;

import java.util.List;

public class OrderDetailResponse {
    public String code;
    public Data data;

    public static class Data {
        public String orderId;
        public String orderHash;
        public double totalAmount;
        public String totalAmountCurrency;
        public Contact contact;
        public List<Payment> payment;

        public String getVirtualAccountNo() {
            return payment != null && !payment.isEmpty() ? payment.get(0).referenceNumber : null;
        }

        public long getTotalAmountAsLong() {
            return Math.round(totalAmount);
        }
    }

    public static class Contact {
        public String phoneNumber;

        public String getCustomerNo() {
            if (phoneNumber == null) return null;
            return phoneNumber.startsWith("+62") ? "0" + phoneNumber.substring(3) : phoneNumber;
        }
    }

    public static class Payment {
        public String referenceNumber;
    }
}