package com.tiket.pojo.ttd;

import java.util.ArrayList;

public class TTDLandingPagePOJO {
    public Data[] data;
    public class AvailabilityPeriods{
        public Object startDate;
        public Object endDate;
    }

    public class BannerContent{
        public String clickUrl;
        public String imageUrl;
        public String subtitle;
        public String title;
    }

    public class Benefit{
        public String text;
        public String colorConfig;
        public String iconUrl;
    }

    public class Card{
        public String pageModuleTitle;
        public String chipsName;
        public String chipsId;
        public String productName;
        public Object starRating;
        public Object availableRoom;
        public ArrayList<Image> images;
        public RateInfo rateInfo;
        public Ribbon ribbon;
        public String label;
        public String location;
        public LocationDetail locationDetail;
        public Reviews reviews;
        public String reviewsPlaceholder;
        public Object loyaltyBenefit;
        public Object promo;
        public Object productTags;
        public ArrayList<Benefit> benefits;
        public AvailabilityPeriods availabilityPeriods;
        public Object propertyType;
        public Object productIcon;
        public Object passengerAmount;
        public Object baggageAmount;
        public Object route;
        public Object productDate;
        public Object duration;
        public Object note;
        public String actionUrl;
        public Object trackerNameGroup;
        public boolean soldOut;
        public String productId;
        public Category category;
        public Object preferredPartner;
        public Object direct;
        public Object isDirect;
    }

    public class Chip{
        public String id;
        public String text;
        public String buttonUrl;
        public String buttonText;
        public String productType;
        public ArrayList<BannerContent> bannerContents;
    }

    public static class Data{
        public String id;
        public String templateCode;
        public String processType;
        public boolean isAnchor;
        public ArrayList<DynamicContent> dynamicContents;
        public int expiredTime;
        public String currency;
        public ModuleDetail moduleDetail;
        public String sectionName;
    }

    public class DynamicContent{
        public ArrayList<Chip> chips;
        public String title;
        public ArrayList<Card> cards;
    }

    public class Heading{
        public String lang;
        public String title;
        public String subtitle;
    }

    public class Image{
        public Object caption;
        public String url;
        public String mobileUrl;
        public int orderNumber;
    }

    public class LocationDetail{
        public String areaID;
        public String areaName;
        public String cityID;
        public String cityName;
        public String regionID;
        public String regionName;
        public String countryID;
        public String countryName;
    }

    public class ModuleDetail{
        public String cardSize;
        public Heading heading;
        public String noBackground;
        public double numberOfVisibleItem;
        public String pageModuleCode;
        public String bannerText;
        public double desktopNumberOfVisibleItem;
        public String contentType;
        public Ratio ratio;
        public String title;

    }

    public class RateInfo{
        public String currency;
        public long basePrice;
        public long discountedPrice;
        public double formattedPrice;
        public Object placeHolder;
        public Object unit;
        public Object rateLabel;
        public int scale;
    }

    public class Ratio{
        public int x;
        public int y;
    }

    public class Reviews{
        public double rating;
        public int count;
    }

    public class Ribbon{
        public String text;
        public String textColor;
        public String backgroundColor;
        public String foldColor;
    }

    public class Root{
        public String code;
        public String message;
        public Object errors;
        public ArrayList<Data> data;
        public long serverTime;
    }
    public class Category{
        public String id;
        public String name;
    }
}
