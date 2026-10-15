package com.tiket.pojo.cartransfer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SearchATResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public SearchQuery searchQuery;
        public Object specialSection;
        public ArrayList<Filter> filter;
        public ArrayList<Fleet> fleets;
        public String message;
        public boolean isMultiRegion;
        public String regionName;
        public ArrayList<String> banners;
        public String additionalInfo;
    }

    public static class SearchQuery{
           public String regionalId;
           public String startDate;
           public int totalDays;
           public String driverOption;
           public int carQuantity;
           public String currency;
           public String pickupTime;
           public BigDecimal latitude;
           public BigDecimal longitude;
    }

    public static class Filter{
        public String type;
        public String name;
        public String backgroundImage;
        public String backgroundColor;
        public String description;
    }

    public static class Fleet{
        public String name;
        public String brandId;
        public String brandName;
        public String modelId;
        public String modelName;
        public String modelType;
        public ArrayList<Object> capacities;
        public String imageUrl;
        public double initialPrice;
        public String currency;
        public int totalDays;
        public String regionalId;
        public int carQuantity;
        public double finalPrice;
        public boolean isSpecialSection;
        public boolean isCampaign;
        public String campaignId;
        public String transmission;
        public int priorityScore;
        public String type;
        public ArrayList<Object> ribbon;
        public String carRegionalId;
    }
}
