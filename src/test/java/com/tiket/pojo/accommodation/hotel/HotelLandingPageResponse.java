package com.tiket.pojo.accommodation.hotel;

import java.util.List;
public class HotelLandingPageResponse {

    public String code;
    public String message;
    public Object errors;
    public List<Data> data;

    public static class Data {
        public String id;
        public String templateCode;
        public String processType;
        public boolean isAnchor;
        public List<DynamicContent> dynamicContents;
        public long expiredTime;
        public String currency;
        public ModuleDetail moduleDetail;
        public String sectionName;
        public String segmentationCode;
    }

    public static class DynamicContent {
        public String productName;  // Added based on the structure with rateInfo
        public RateInfo rateInfo;  // rateInfo found here in your JSON structure
        public List<Chip> chips;
        public List<Card> cards;
    }

    public static class RateInfo {
        public String currency;
        public double basePrice;
        public double discountedPrice;
    }

    public static class Chip {
        public String id;
        public String text;
        public String buttonUrl;
        public String buttonText;
        public String productType;
    }

    public static class Card {
        public String productName;
        public RateInfo rateInfo;
    }

    public static class ModuleDetail {
        public String cardSize;
        public Heading heading;
        public String noBackground;
        public String icon;
    }

    public static class Heading {
        public String lang;
        public String title;
        public String subtitle;
    }
}