package com.tiket.pojo.accommodation.homes;

import java.util.ArrayList;

public class HomesLandingPagePriceData {

    public Datum data;
    public Root root;

    public class BannerContent{
        public String clickUrl;
        public String imageUrl;
        public String subtitle;
        public String title;

    }

    public class Card{
        public String chipsId;
        public String productName;
        public int starRating;
        public Object availableRoom;
        public ArrayList<Image> images;
        public RateInfo rateInfo;
        public Ribbon ribbon;
        public Object label;
        public String location;
        public LocationDetail locationDetail;
        public Reviews reviews;
        public String reviewsPlaceholder;
        public Object loyaltyBenefit;
        public Object promo;
        public Object productTags;
        public ArrayList<Object> benefits;
        public Object availabilityPeriods;
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
        public PreferredPartner preferredPartner;
        public ArrayList<String> roomStatistic;
        public Object isDirect;
    }

    public class Category{
        public String id;
        public String name;
    }

    public class Chip{
        public ArrayList<BannerContent> bannerContents;
        public String text;
        public String id;
        public String buttonUrl;
        public String buttonText;
        public String productType;
        public ArrayList<Object> generalPeriod;
    }

    public class Datum{
        public String id;
        public String templateCode;
        public String processType;
        public boolean isAnchor;
        public ArrayList<DynamicContent> dynamicContents;
        public int expiredTime;
        public String currency;
        public ModuleDetail moduleDetail;
        public String sectionName;
        public String segmentationCode;
    }

    public class DynamicContent{
        public String productName;
        public String productType;
        public int starRating;
        public Object availableRoom;
        public ArrayList<Image> images;
        public RateInfo rateInfo;
        public String location;
        public LocationDetail locationDetail;
        public Reviews reviews;
        public String reviewsPlaceholder;
        public Object availabilityPeriods;
        public String actionUrl;
        public boolean soldOut;
        public String productId;
        public Ribbon ribbon;
        public Category category;
        public PreferredPartner preferredPartner;
        public String imageUrl;
        public String text;
        public ArrayList<Object> generalPeriod;
        public ArrayList<InternalLink> internalLinks;
        public ArrayList<Chip> chips;
        public String title;
        public ArrayList<Card> cards;
        public Object variantName;
        public Object relevantActiveOrder;
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

    public class Image{
        public String caption;
        public String url;
        public String mobileUrl;
        public int orderNumber;
    }

    public class InternalLink{
        public String link;
        public String text;
        public String subText;
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
        public Heading heading;
        public String noBackground;
        public String icon;
        public String description;
        public String label;
        public String title;
        public String contentType;
        public String imageAlignment;
        public String useTab;
        public boolean useSubtext;
        public String pageModuleCode;
        public GridDesktop gridDesktop;
        public Grid grid;
        public int ratioX;
        public String bannerText;
        public int ratioY;
        public Ratio ratio;
        public double numberOfVisibleItem;
        public double desktopNumberOfVisibleItem;
        public String cardSize;
        public String iconUrl;
        public String seeAllUrl;
        public int dayPlusPrice;
        public boolean generalAvailability;
        public String seeAll;
    }

    public class PreferredPartner{
        public String tier;
        public String icon;
    }

    public class RateInfo{
        public String currency;
        public int basePrice;
        public int discountedPrice;
        public Object placeHolder;
        public Object unit;
        public Object rateLabel;
        public int scale;
        public String remark;
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
        public ArrayList<Datum> data;
        public long serverTime;
        public SessionData sessionData;
    }

    public class SessionData{
        public String username;
        public String firstname;
        public String lastname;
        public Object lang;
        public String currency;
        public int tixPoint;
    }
}
