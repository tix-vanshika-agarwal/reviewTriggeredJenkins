package com.tiket.pojo.ttd;

import java.util.ArrayList;

public class GETLandingPageForYouSectionPOJO {

    public String code;
    public String message;
    public Object errors;
    public Data[] data;

    public static class Data {
        public String id;
        public String currency;
        public String paymentCurrency;
        public int scale;
        public Object currencyRate;
        public ArrayList<Double> longLat;
        public int popularityScore;
        public Object dynamicPopularityScore;
        public String timeZoneId;
        public String productType;
        public ArrayList<Translation> translations;
        public ArrayList<Image> images;
        public ArrayList<Image> supplierImages;
        public Boolean isEditorsPick;
        public Boolean isBPG;
        public Boolean isOnlineExperience;
        public Boolean isTiketFlexi;
        public String availabilityStatus;
        public String primaryCategory;
        public ArrayList<String> categories;
        public ArrayList<ProductCategory> productCategories;
        public ArrayList<Subcategory> subcategories;
        public ArrayList<String> partnerIds;
        public Object loyaltyMemberLevel;
        public Campaign campaign;
        public long startingMarketPriceInCents;
        public long startingPriceInCents;
        public long startingGimmickPriceInCents;
        public int startingCrossedPrice;
        public int startingFinalPrice;
        public String startingFinalPricePackageCodePriceTierCode;
        public double bestDiscountInPercent;
        public String earliestSaleStartDate;
        public String latestSaleEndDate;
        public String earliestAvailabilityDate;
        public String latestAvailabilityDate;
        public String earliestBookingDate;
        public Object instantPassType;
        public Object closingTime;
        public Object startingPricePackageAdditionalFee;
        public Object openDayTimes;
        public double avgRating;
        public int commentCount;
        public boolean isInstantConfirmation;
        public AtlasLocation atlasLocation;
        public int startingGimmickPriceInCentsIDR;
        public boolean isRefundable;
        public String displayRefundTier;
        public Object featuredLanguage;
        public Object totalLanguagesSupported;
        public int booked;
        public Object subcategoryGroup;
        public Object landscapeTransportMapImages;
        public ArrayList<Object> portraitTransportMapImages;
    }

    public static class Translation {
        public String language;
        public String url;
        public String title;
        public String area;
        public String city;
        public String region;
        public String country;
        public Object extraBenefit;
        public Object promoLabel;
        public Object marketingLabel;
    }

    public static class Image {
        public String urlSmall;
        public String urlMedium;
        public ArrayList<ImageTranslation> translations;
        public Object supplierCode;
        public Object isHidden;
        public Object videoUrl;
    }

    public static class ImageTranslation {
        public String language;
        public String alt;
        public Object description;
    }

    public static class ProductCategory {
        public String code;
        public String status;
        public ArrayList<CategoryTranslation> translations;
        public Icon icon;
        public boolean isPrimaryCategory;
        public Banner banner;
        public ArrayList<String> displayPlacements;
        public boolean locationFilter;
    }

    public static class CategoryTranslation {
        public String language;
        public String name;
        public String description;
    }

    public static class Icon {
        public Object urlSource;
        public String urlSmall;
        public String urlMedium;
        public String urlLarge;
        public ArrayList<ImageTranslation> translations;
        public Object showInGallery;
    }

    public static class Banner {
        public Object urlSource;
        public String urlSmall;
        public String urlMedium;
        public String urlLarge;
        public ArrayList<ImageTranslation> translations;
        public Object showInGallery;
    }

    public static class Subcategory {
        public String id;
        public String code;
        public String productCategory;
        public Category category;
        public ArrayList<CategoryTranslation> translations;
    }

    public static class Category {
        public String code;
        public String status;
        public ArrayList<CategoryTranslation> translations;
        public Icon icon;
        public boolean isPrimaryCategory;
        public Banner banner;
        public ArrayList<String> displayPlacements;
        public boolean locationFilter;
    }

    public static class Campaign {
        public ArrayList<CampaignTranslation> translations;
        public CampaignImage image;
        public Label label;
    }

    public static class CampaignTranslation {
        public String language;
        public String name;
        public String description;
    }

    public static class CampaignImage {
        public Object urlSource;
        public String urlSmall;
        public String urlMedium;
        public String urlLarge;
        public ArrayList<ImageTranslation> translations;
        public Object showInGallery;
    }

    public static class Label {
        public ArrayList<LabelTranslation> translations;
        public String textColor;
        public String backgroundColor;
        public String foldColor;
    }

    public static class LabelTranslation {
        public String language;
        public String text;
        public String name;
    }

    public static class AtlasLocation {
        public String countryCode;
        public String regionCode;
        public String cityCode;
        public String areaCode;
    }
}
