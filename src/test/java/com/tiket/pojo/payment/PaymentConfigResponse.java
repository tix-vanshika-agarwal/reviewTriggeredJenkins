package com.tiket.pojo.payment;

import java.util.List;
import java.util.Map;

public class PaymentConfigResponse {
    public Data data;

    public static class Data {
        public List<PaymentConfig> content;
    }

    public static class PaymentConfig {
        public String id;
        public String paymentName;
        public boolean paymentStatus;
        public Map<String, Boolean> statusPerChannel;
    }
}
