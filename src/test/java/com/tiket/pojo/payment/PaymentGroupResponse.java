package com.tiket.pojo.payment;

import java.util.List;

public class PaymentGroupResponse {
    public String code;
    public String message;
    public Data data;

    public static class Data {
        public List<PaymentGroup> commonGroup;
        public PromotedGroup promotedGroup;
    }

    public static class PaymentGroup {
        public String id;
        public Title title;
        public boolean status;
        public List<PaymentMethodItem> items;
    }

    public static class Title {
        public String en;
        public String id;
    }

    public static class PromotedGroup {
        public String title;
        public String subtitle;
        public List<PaymentMethodItem> items;
    }

    public static class PaymentMethodItem {
        public boolean status;
        public String paymentMethod;
        public String paymentName;
        public String label;
    }
}
