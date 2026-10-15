package com.tiket.pojo.room;

import java.util.ArrayList;

public class SelectRoomResponse{

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data{
        public ArrayList<Room> rooms;
        public ArrayList<PaymentOptionList> paymentOptionList;
        public ArrayList<FilterSummary> filterSummary;
        public ArrayList<Usp> usp;
        public Object nextAvailableDates;
        public CurrencyData currencyData;
        public String accommodationType;
        public ArrayList<Object> bannerList;
        public Object warningList;
        public HotelNudge hotelNudge;
        public RoomRecommendation roomRecommendation;
        public int maxSelectedRatePlan;
        public Object loyaltyInfo;
    }

    public static class Room{
        public String groupName;
        public String groupingKey;
        public String groupDesign;
        public ArrayList<RatePlan> ratePlans;
        public ArrayList<Promo> promos;
        public ArrayList<GroupNudge> groupNudges;
        public GroupingVariablesInfo groupingVariablesInfo;
        public ArrayList<GroupFacility> groupFacilities;
        public ArrayList<String> primaryGroupFacilities;
        public ArrayList<PopularFacility> popularFacilities;
        public String childrenPolicy;
        public Object roomDescription;
        public String roomRecommendationFeatures;
        public ArrayList<Image> images;
        public Object descriptionRoom;
    }

    public static class RatePlan{
        public String roomId;
        public String rateCode;
        public RatePlanNudge ratePlanNudge;
        public ArrayList<FeatureLabel> featureLabels;
        public CancellationPolicies cancellationPolicies;
        public int availableRoom;
        public int maxAdultOccupancy;
        public int maxChildOccupancy;
        public int alternateAdult;
        public int alternateChild;
        public int numberOfRoom;
        public String restrictions;
        public String paymentOption;
        public String accommodationNudge;
        public RateInfo rateInfo;
        public ArrayList<ValueAdded> valueAdded;
        public ArrayList<Object> promos;
        public ArrayList<RoomFilter> roomFilter;
        public Object itemColor;
        public String specialCheckInInstructions;
        public Object corporate;
        public String disclaimer;
        public boolean isAlternate;
        public boolean isRoomRecommendation;
    }

    public static class RatePlanNudge{
        public String title;
        public String iconUrl;
        public String textColor;
        public String backgroundColor;
        public boolean outline;
    }

    public static class FeatureLabel{
        public String text;
        public String backgroundColor;
        public String textColor;
        public String type;
    }

    public static class CancellationPolicies{
        public String iconUrl;
        public String text;
        public String textColor;
        public String type;
        public Object description;
        public ArrayList<Policy> policies;
        public Object additionalItems;
    }

    public static class Policy{
        public String iconUrl;
        public String text;
        public String type;
    }

    public static class RateInfo{
        public Object loyalty;
        public Price price;
        public int tixPoints;
        public PriceSummary priceSummary;
        public LoginPrice loginPrice;
        public TixPointsV2 tixPointsV2;
        public boolean isPriceAfterCashback;
    }

    public static class TixPointsV2{
        public double baseTotal;
        public double basePerRoomPerNight;
        public double cashbackTotal;
        public double cashbackPerRoomPerNight;
    }

    public static class Price{
        public int baseRateWithTax;
        public int rateWithTax;
        public int totalBaseRateWithTax;
        public int totalRateWithTax;
        public int rateWithoutTax;
        public int baseRateWithoutTax;
        public int totalRateWithoutTax;
        public int totalBaseRateWithoutTax;
    }

    public static class PriceSummary{
        public int total;
        public int totalWithoutTax;
        public int taxAndOtherFee;
        public int taxFeeRoom;
        public ArrayList<PricePerNight> pricePerNight;
        public ArrayList<Object> internalBreakdown;
    }

    public static class PricePerNight{
        public String stayingDate;
        public int rate;
    }

    public static class LoginPrice{
        public double rateWithTax;
        public double totalRateWithTax;
        public double rateWithoutTax;
        public double totalRateWithoutTax;
        public String nudgeType;
        public boolean allowToDisplayNudge;
        public boolean allowToDisplayLoginPrice;
    }

    public static class ValueAdded{
        public String title;
        public ArrayList<ValueAddedDetail> details;
    }

    public static class ValueAddedDetail{
        public String title;
        public Object items;
    }

    public static class RoomFilter{
        public String key;
        public String name;
        public boolean value;
    }

    public static class Promo{
        public String text;
        public String type;
        public Object imageUrl;
    }

    public static class GroupNudge{
        public String text;
        public String backgroundColor;
        public String textColor;
    }

    public static class GroupingVariablesInfo{
        public ArrayList<BedType> bedTypes;
        public SmokingPreferences smokingPreferences;
        public RoomSize roomSize;
    }

    public static class BedType{
        public Object roomCategory;
        public String bedType;
    }

    public static class SmokingPreferences{
        public String iconUrl;
        public String text;
    }

    public static class RoomSize{
        public int size;
        public String unit;
    }

    public static class GroupFacility{
        public String text;
        public String iconUrl;
        public ArrayList<String> detail;
    }

    public static class PopularFacility{
        public String iconUrl;
        public String text;
    }

    public static class Image{
        public String caption;
        public String url;
        public String mobileUrl;
        public Object orderNumber;
        public String name;
    }

    public static class PaymentOptionList{
        public String id;
        public String name;
        public String subName;
        public String description;
        public String icon;
        public String disclaimer;
    }

    public static class FilterSummary{
        public String key;
        public String name;
    }

    public static class Usp{
        public String id;
        public String title;
        public String subTitle;
        public String icon;
    }

    public static class CurrencyData{
        public String code;
        public int scale;
    }

    public static class HotelNudge{
        public String text;
        public String textColor;
    }

    public static class RoomRecommendation{
        public String iconUrl;
        public String title;
    }
}