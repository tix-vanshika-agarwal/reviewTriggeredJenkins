package com.tiket.pojo.carrental;
import java.util.List;

public class SelectCarRentalResponse {
    public String code;
    public String message;
    public String errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public SearchQuery searchQuery;
        public SpecialSection specialSection;
        public List<Filter> filter;
        public List<Fleets> fleets;
        public String message;
        public boolean isMultiRegion;
        public String regionName;
        public List<Banners> banners;
        public String additionalInfo;
    }

    public static class SearchQuery {
        public String regionalId;
        public String startDate;
        public int totalDays;
        public String driverOption;
        public int carQuantity;
        public String currency;
        public String pickupTime;
        public String latitude;
        public String longitude;
    }

    public static class SpecialSection {
        public String iconUrl;
        public String title;
        public String bgColor;
        public String fontColor;
        public String expiryTime;
        public String backgroundImage;
        public String timeZone;
        public int maxContent;
    }

    public static class Filter {
        public String type;
        public String name;
        public String backgroundImage;
        public String backgroundColor;
        public String description;
    }

    public static class Fleets {
        public String name;
        public String brandId;
        public String brandName;
        public String modelId;
        public String modelName;
        public String modelType;
        public Capasities capasities;
        public String imageUrl;
        public String initialPrice;
        public String currency;
        public int totalDays;
        public String regionalId;
        public int carQuantity;
        public String finalPrice;
        public boolean isSpecialSection;
        public boolean isCampaign;
        public String campaignId;
        public String transmission;
        public int priorityScore;
        public String type;
        public List<Ribbon> ribbon;
        public String carRegionalId;
    }

    public static class Banners {

    }

    public static class Capasities {
        public String key;
        public String iconUrl;
        public String value;
    }

    public static class Ribbon {
        public String iconUrl;
        public String title;
        public String bgColor;
        public String fontColor;
        public String description;
    }
}