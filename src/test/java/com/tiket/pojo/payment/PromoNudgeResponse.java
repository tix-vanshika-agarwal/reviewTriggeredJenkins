package com.tiket.pojo.payment;

import java.util.List;

public class PromoNudgeResponse {
    public Data data;

    public static class Data {
        public List<PromoNudge> content;
    }

    public static class PromoNudge {
        public String id;
        public String currency;
        public String paymentMethod;
        public ActiveChannels activeChannels;
        public boolean enableCustom;
        public CustomText customText;
    }

    public static class ActiveChannels {
        public boolean ios;
        public boolean android;
        public boolean mobile;
        public boolean desktop;
    }

    public static class CustomText {
        public String en;
        public String id;
    }
}
