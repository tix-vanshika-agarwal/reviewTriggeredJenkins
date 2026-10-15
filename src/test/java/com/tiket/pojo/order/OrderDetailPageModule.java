package com.tiket.pojo.order;

import java.util.ArrayList;

/**
 * Gson model for GET order-detail page-modules API.
 * Dynamic content shape varies by {@link PageModules#templateCode}; optional fields remain null when not present.
 */
public class OrderDetailPageModule {

    public String code;
    public String message;
    public Object errors;
    public ArrayList<PageModules> data;
    public Long serverTime;

    public class PageModules {
        public String id;
        public String templateCode;
        public String processType;
        public Boolean isAnchor;

        public ArrayList<DynamicContent> dynamicContents;
        public Long expiredTime;
        public String currency;
        public Boolean useCustomDataSource;
        public ModuleDetail moduleDetail;
        public String sectionName;
        public String segmentationCode;
    }

    /**
     * Union of payloads under {@link PageModules#dynamicContents} (e.g. GV_SECTION, IMAGE_WITH_CHIPS,
     * BANNER_GRID, RECENT_WIDGET, BANNER_LOYALTY).
     */
    public class DynamicContent {
        /* IMAGE_WITH_CHIPS / BANNER_GRID */
        public ArrayList<Chip> chips;

        public String title;
        public ArrayList<ImageWithChipsCard> cards;
        public String variantName;
        public String relevantActiveOrder;
        public BriefLocation location;
        public Boolean showNearbyAirport;

        /* GV_SECTION voucher rows */
        public String id;
        public String icon;
        public String url;
        public Long expiredAt;
        public String refId;

        /* RECENT_WIDGET */
        public String vertical;
        public ArrayList<RecentContent> contents;

        /* BANNER_LOYALTY */
        public String clickUrl;
        public String imageUrl;
        public Boolean ulpState;
        public String ulpTier;

        /* Legacy / other templates */
        public String description;
        public String buttonTitle;
        public String imageBackground;
        public String buttonText;
        public String contentTitle;
        public String contentDescription;
        public String buttonUrl;
        public String tabText;
        public String productName;
        public String reviewsPlaceholder;
        public RateInfo rateInfo;
        public Reviews reviews;
    }

    public class Chip {
        public String id;
        public String text;
        public String buttonUrl;
        public String buttonText;
        public String buttonStyle;
        public String productType;
        public String parameters;
        public String processType;
        public String cardType;
        public String seeAllImage;
        public String imageLabel;
        public ArrayList<BannerContents> bannerContents;
        /** Older home-style modules */
        public ArrayList<CardDefault> cardDefaults;
    }

    public static class BannerContents {
        public String clickUrl;
        public String imageUrl;
        public String subtitle;
        public String title;
        /** Present on some legacy responses */
        public String productType;
        public ArrayList<Object> generalPeriod;
        public ArrayList<CardDefault> cardDefaults;
    }

    public static class CardDefault {
        public String image;
        public String contentDescription;
        public String contentTitle;
        public String url;
    }

    public static class BriefLocation {
        public String name;
        public String cityName;
        public String countryName;
    }

    public static class ImageWithChipsCard {
        public String chipsId;
        public String productName;
        public Integer starRating;
        public Integer availableRoom;
        public ArrayList<CardImage> images;
        public RateInfo rateInfo;
        public Ribbon ribbon;
        public String label;
        public String location;
        public LocationHierarchy locationDetail;
        public Reviews reviews;
        public String reviewsPlaceholder;
        public Object loyaltyBenefit;
        public Object promo;
        public Object productTags;
        public Object benefits;
        public Object availabilityPeriods;
        public String propertyType;
        public Object productIcon;
        public Object passengerAmount;
        public Object baggageAmount;
        public Object route;
        public Object productDate;
        public Object duration;
        public Object note;
        public String actionUrl;
        public Object trackerNameGroup;
        public Boolean soldOut;
        public String productId;
        public Object category;
        public Object preferredPartner;
        public Object roomStatistic;
        public String productType;
        public Object accommodationAttributes;
        public Object transmissionType;
        public Object transmissionIconUrl;
        public Object passengerNumber;
        public Object baggageNumber;
        public Object isDirect;
    }

    public static class CardImage {
        public String caption;
        public String url;
        public String mobileUrl;
        public Integer orderNumber;
    }

    public static class Ribbon {
        public String text;
        public String textColor;
        public String backgroundColor;
        public String foldColor;
    }

    public static class LocationHierarchy {
        public String areaID;
        public String areaName;
        public String cityID;
        public String cityName;
        public String regionID;
        public String regionName;
        public String countryID;
        public String countryName;
    }

    /**
     * {@link PageModules#moduleDetail} merges keys from GV, IMAGE_WITH_CHIPS, BANNER_GRID,
     * RECENT_WIDGET, BANNER_LOYALTY, etc.; unused entries stay null.
     */
    public class ModuleDetail {
        /* Common */
        public Heading heading;
        public String theme;
        public String noBackground;

        /* GV_SECTION */
        public Object seeAll;
        public String logoutState;
        public String background;
        public String icon;
        public Object showClaimedGV;
        public String maxVoucher;

        /* IMAGE_WITH_CHIPS */
        public String cardSize;
        public Integer dayPlusPrice;
        public Boolean generalAvailability;
        public ModuleTooltip tooltip;

        /* BANNER_GRID */
        public GridDimensions gridDesktop;
        public GridDimensions grid;
        public GridDimensions ratio;
        public String backgroundColor;
        public String interactionType;
        public String pageModuleCode;
        public Integer ratioX;
        public Integer ratioY;
        public String bannerText;
        public Boolean canAppendDeeplink;

        /* RECENT_WIDGET */
        public ArrayList<String> recentSection;
        public ArrayList<String> selectVertical;

        /* Older shape */
        public String title;
        public String description;
        public String label;
        public String seeAllUrl;
        public Cta cta;
    }

    public static class GridDimensions {
        public Integer x;
        public Integer y;
    }

    public static class ModuleTooltip {
        public TooltipCaption awardLabel;
        public TooltipCaption starRating;
        public TooltipCaption flowerRating;
    }

    public static class TooltipCaption {
        public String title;
        public String subtitle;
    }

    public static class RecentContent {
        public String type;
        public String title;
        public Long startDate;
        public Long endDate;
        public String deepLink;
        public String imageUrl;
        public RecentAttributes attributes;
        public String inventoryId;
        public String titleType;
    }

    public static class RecentAttributes {
        public String partnerType;
        public String partnerTypeIconUrl;
        public String location;
        public String inventoryType;
        public Integer room;
        public Integer guest;
        public String cabinClass;
        public Integer passenger;
        public String tripType;
    }

    public static class Cta {
        public String title;
        public String url;
    }

    public static class Heading {
        public String lang;
        public String title;
        public String subtitle;
    }

    public static class RateInfo {
        public String currency;
        /** API returns numbers; Gson maps them from JSON numbers. */
        public Long basePrice;
        public Long discountedPrice;
        public String placeHolder;
        public String unit;
        public String rateLabel;
        public Integer scale;
        public String remark;
    }

    public static class Reviews {
        public String rating;
    }
}
