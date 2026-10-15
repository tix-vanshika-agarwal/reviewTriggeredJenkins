package com.tiket.pojo.utilities;

import java.util.ArrayList;

public class HomePageModule {
    public Data data;

    public class Data {
        public ArrayList<PageModules> pageModules;
        public int chatUnreadCount;
        public InboxUnreadCount inboxUnreadCount;
        public FinanceBarItem blibliTiketPoint;
        public FinanceBarItem paylater;
        public FinanceBarItem blipay;
        public VerticalIcons verticalIcons;
    }

    public static class VerticalIcons{
        public String sequenceId;
        public ArrayList<Vertical> verticals;
        public Object primaryId;
    }

    public static class Vertical{
        public String id;
        public String code;
        public String name;
        public String altText;
        public Label label;
        public String typeAction;
        public String iconUrl;
        public String clickUrl;
        public boolean isPromptUpdate;
        public String trackerCode;
        public String currency;
    }

    public static class Label{
        public String color;
        public String value;
    }

    public class FinanceBarItem{
        public ArrayList<Content> contents;
        public Content content;
    }

    public class Content{
        public String text;
        public String textColor;
    }

    public class InboxUnreadCount{
        public int totalCount;
        public Object categoriesCount;
    }

    public class PageModules {
        public String id;
        public String templateCode;
        public String processType;

        public ArrayList<DynamicContent> dynamicContents;
        public long expiredTime;
        public String currency;
        public ModuleDetail moduleDetail;
        public String sectionName;

    }

    public class DynamicContent{
        public ArrayList<Chip> chips;
        public String title;
        public String url;
        public String description;
        public String buttonTitle;
        public String imageBackground;
        public String buttonText;
        public String contentTitle;
        public String contentDescription;
        public String buttonUrl;
        public String tabText;
        public String productName;
        /** Recommendation-engine variant that produced the cards, e.g. {@code neoPolaris_v8}. */
        public String variantName;
        public String location;
        public RateInfo rateInfo;
        public Reviews reviews;
        public String reviewsPlaceholder;
        public ArrayList<Object> cards;
    }

    public class Chips{
        public String buttonUrl;
    }

    public class Chip{
        public String id;
        public String text;
        public String buttonUrl;
        public String buttonText;
        public String productType;
        public ArrayList<BannerContents> bannerContents;
        public ArrayList<CardDefault> cardDefaults;
    }

    public static class BannerContents {
        public String clickUrl;
        public String productType;
        public ArrayList<Object> generalPeriod;
        public ArrayList<CardDefault> cardDefaults;
    }

    public static class CardDefault{
        public String image;
        public String contentDescription;
        public String contentTitle;
        public String url;
    }

    public class ModuleDetail{
        public Heading heading;
        public String title;
        public String description;
        public String label;
        public String seeAllUrl;
        public Cta cta;
    }

    public static class Cta{
        public String title;
        public String url;
    }

    public static class Heading {
        public String lang;
        public String title;
        public String subtitle;
    }

    public static class RateInfo {
        public String discountedPrice;
        public String basePrice;
    }

    public static class Reviews {
        public String rating;
    }
}