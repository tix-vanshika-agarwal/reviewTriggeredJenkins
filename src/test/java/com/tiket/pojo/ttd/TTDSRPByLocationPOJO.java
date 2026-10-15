package com.tiket.pojo.ttd;

import java.util.ArrayList;
import java.util.Date;

public class TTDSRPByLocationPOJO {

    public Datum[] data;
    public String message;

    public class AtlasLocation{
        public String countryCode;
        public String regionCode;
        public Object cityCode;
        public Object areaCode;
    }

    public class Banner{
        public Object urlSource;
        public String urlSmall;
        public String urlMedium;
        public String urlLarge;
        public ArrayList<Translation> translations;
        public Object showInGallery;
    }

    public class Category{
        public String code;
        public String status;
        public ArrayList<Translation> translations;
        public Icon icon;
        public boolean isPrimaryCategory;
        public Banner banner;
        public ArrayList<String> displayPlacements;
        public boolean locationFilter;
    }

    public class Datum{
        public String id;
        public String currency;
        public double currencyRate;
        public String paymentCurrency;
        public long scale;
        public ArrayList<Double> longLat;
        public long popularityScore;
        public String timeZoneId;
        public ArrayList<Translation> translations;
        public ArrayList<Image> images;
        public ArrayList<Object> supplierImages;
        public String availabilityStatus;
        public boolean isEditorsPick;
        public boolean isOnlineExperience;
        public boolean isTiketFlexi;
        public String primaryCategory;
        public ArrayList<String> categories;
        public ArrayList<ProductCategory> productCategories;
        public ArrayList<Subcategory> subcategories;
        public Object tiketClean;
        public Object loyaltyMemberLevel;
        public Object campaign;
        public long startingMarketPriceInCents;
        public long startingPriceInCents;
        public long startingGimmickPriceInCents;
        public long startingGimmickPriceInCentsIDR;
        public long startingCrossedPrice;
        public long startingFinalPrice;
        public double bestDiscountInPercent;
        public String earliestSaleStartDate;
        public String latestSaleEndDate;
        public String earliestAvailabilityDate;
        public String latestAvailabilityDate;
        public String earliestBookingDate;
        public Object instantPassType;
        public Object closingTime;
        public ArrayList<Object> openDayTimes;
        public double avgRating;
        public long commentCount;
        public Object videoRental;
        public boolean isInstantConfirmation;
        public AtlasLocation atlasLocation;
        public boolean isBPG;
    }

    public class Icon{
        public Object urlSource;
        public String urlSmall;
        public String urlMedium;
        public String urlLarge;
        public ArrayList<Translation> translations;
        public Object showInGallery;
    }

    public class Image{
        public String urlSmall;
        public String urlMedium;
        public ArrayList<Translation> translations;
        public String supplierCode;
        public Object isHidden;
        public Object videoUrl;
    }

    public class ProductCategory{
        public String code;
        public String status;
        public ArrayList<Translation> translations;
        public Icon icon;
        public boolean isPrimaryCategory;
        public Banner banner;
        public ArrayList<String> displayPlacements;
        public boolean locationFilter;
    }

    public class Root{
        public String code;
        public String message;
        public Object errors;
        public ArrayList<Datum> dataa;
        public Date serverTime;
    }

    public class Subcategory{
        public String id;
        public String code;
        public String productCategory;
        public Category category;
        public ArrayList<Translation> translations;
    }

    public class Translation{
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
        public String alt;
        public Object description;
        public String name;
    }
}
