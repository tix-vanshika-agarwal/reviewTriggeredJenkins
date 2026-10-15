package com.tiket.pojo.ttd;

import java.math.BigInteger;
import java.util.ArrayList;

public class TTDPartnerPageModules {
    public String code;
    public String message;
    public Object errors;
    public ArrayList<Datum> data;
    public BigInteger serverTime;

    public static class BannerContent{
        public String clickUrl;
        public String imageUrl;
        public String subtitle;
        public String title;
    }

    public class CardDefault{
        public String image;
        public String contentDescription;
        public String contentTitle;
        public String url;
    }

    public static class Chip{
        public ArrayList<BannerContent> bannerContents;
        public String id;
        public String buttonText;
        public String text;
        public String buttonUrl;
        public ArrayList<Object> generalPeriod;
        public ArrayList<CardDefault> cardDefaults;
    }

    public static class Datum{
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

    public static class DynamicContent{
        public ArrayList<Chip> chips;
        public String title;
        public String subtitle;
        public String tabName;
        public long countdown;
        public String countdownText;
        public String type;
        public String supergraphicImage;
        public String topColor;
        public String bottomColor;
        public String icon;
        public ArrayList<Card> cards;
        public String imageBackground;
        public String buttonText;
        public String contentTitle;
        public String contentDescription;
        public String buttonUrl;
        public String tabText;
    }

    public static class Card{
        public String chipsId;
        public String productName;
        public int starRating;
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
        public Object category;
        public PreferredPartner preferredPartner;
        public ArrayList<String> roomStatistic;
        public Object direct;
        public Object isDirect;
    }

    public class Image{
        public String caption;
        public String url;
        public String mobileUrl;
        public int orderNumber;
    }

    public class RateInfo{
        public String currency;
        public int basePrice;
        public int discountedPrice;
        public String placeHolder;
        public Object unit;
        public String rateLabel;
        public int scale;
    }

    public class Ribbon{
        public String text;
        public String textColor;
        public String backgroundColor;
        public String foldColor;
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

    public class Reviews{
        public double rating;
        public int count;
    }

    public class AvailabilityPeriods{
        public Object startDate;
        public Object endDate;
    }

    public class Benefit{
        public String text;
        public String colorConfig;
        public String iconUrl;
    }

    public class Category{
        public String id;
        public String name;
    }

    public class PreferredPartner{
        public String tier;
        public String icon;
    }


    public class Grid{
        public int x;
        public int y;
    }

    public class GridDesktop{
        public int x;
        public int y;
    }

    public class Heading{
        public String lang;
        public String title;
        public String subtitle;
    }

    public class ModuleDetail{
        public String backgroundColor;
        public Heading heading;
        public String pageModuleCode;
        public GridDesktop gridDesktop;
        public Grid grid;
        public int ratioX;
        public String bannerText;
        public int ratioY;
        public Ratio ratio;
        public String noBackground;
        public String seeAllUrl;
        public String backgroundUrl;
        public String cardSize;
        public String seeAll;
        public String useChip;
        public double numberOfVisibleItem;
        public String imageHuman;
        public String contentType;
        public double desktopNumberOfVisibleItem;
    }

    public class Ratio{
        public int x;
        public int y;
    }
}
