package com.tiket.pojo.accommodation.hotel;

import java.util.ArrayList;
import java.util.Date;

public class PreBookGeneratingCartResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class AddOn{
        public int id;
        public String name;
        public String description;
        public String type;
        public String imageUrl;
        public String thumbnailUrl;
        public int minAmount;
        public int maxAmount;
        public RateInfo rateInfo;
        public ArrayList<AddOnsForm> addOnsForm;
        public double tixPoint;
        public ArrayList<String> availableRoomIdentifier;
    }

    public static class AddOnCategoryGroup{
        public String name;
        public String iconUrl;
        public String info;
        public String description;
        public ArrayList<AddOn> addOns;
        public String category;
    }

    public static class AddOnsForm{
        public String name;
        public String placeholder;
        public String caption;
        public String title;
        public String type;
        public ArrayList<Validator> validators;
        public String referenceField;
        public String inputSourceURL;
        public ArrayList<Object> inputSources;
        public Object disclaimer;
    }

    public static class Area{
        public String id;
        public String name;
    }

    public static class BedType{
        public String roomCategory;
        public String bedType;
    }

    public static class BenefitAdded{
        public String icon;
        public String name;
    }

    public static class BenefitList{
        public String icon;
        public String text;
    }

    public static class Breakdown{
        public String title;
        public double value;
        public String type;
    }

    public static class CancellationPoliciesV2{
        public double amount;
        public Date time;
    }

    public static class CancellationPoliciesV3{
        public String policyContent;
        public Object additionalItems;
        public ArrayList<Policy> policies;
        public PolicyToShow policyToShow;
        public boolean refundable;
    }

    public static class CancellationPolicyInfo{
        public String title;
        public String subTitle;
        public String icon;
    }

    public static class City{
        public String id;
        public String name;
    }

    public static class Compensation{
        public String coverage;
        public String compensation;
    }

    public static class Coordinates{
        public double latitude;
        public double longitude;
    }

    public static class Country{
        public String id;
        public String name;
    }

    public static class CurrencyData{
        public String code;
        public int scale;
    }

    public static class Data{
        public CurrencyData currencyData;
        public ArrayList<Hotel> hotels;
        public DictionaryImages dictionaryImages;
        public ArrayList<InsuranceV2> insuranceV2;
        public Object freeProtection;
        public ArrayList<Surcharge> surcharge;
        public Object footerBanner;
        public Object popup;
        public String interimPageUrlPath;
        public boolean isSalutationEnabled;
    }

    public static class Detail{
        public String hotelId;
        public String name;
        public String address;
        public String postalCode;
        public double starRating;
        public Country country;
        public Region region;
        public City city;
        public Area area;
        public Location location;
        public String phone;
        public Object fax;
        public MainImage mainImage;
        public boolean lpg;
        public Object covidInformation;
        public GeneralInfo generalInfo;
        public String accommodationType;
        public ArrayList<Room> rooms;
        public Object placeType;
    }

    public static class DictionaryImages{
        public String bgBenefitBanner;
        public String icBenefitBanner;
        public String supergraphicBlue;
        public String bgBenefitForYou;
        public String supergraphicGradientGreen;
        public String supergraphicGradientYellow;
    }

    public static class Discount{
        public String type;
        public Object promoText;
        public double value;
        public Object promoLabelId;
        public int expiredDate;
        public Object imageUrl;
        public Object promoIcon;
        public boolean highestPriority;
    }

    public static class FeatureAdded{
        public String icon;
        public String name;
        public String type;
        public boolean available;
    }

    public static class GeneralInfo{
        public String checkIn;
        public String checkOut;
        public String policy;
    }

    public static class GroupFacility{
        public String name;
        public String icon;
        public ArrayList<String> detail;
    }

    public static class Hotel{
        public String startDate;
        public String endDate;
        public int night;
        public ArrayList<AddOnCategoryGroup> addOnCategoryGroups;
        public ArrayList<PaymentOptionList> paymentOptionList;
        public Object taxDescription;
        public Detail detail;
        public Object giftVouchers;
    }

    public static class InputSource{
        public String name;
        public String label;
        public String value;
        public Object image;
        public Object price;
        public Object tixPoint;
        public ValueRange valueRange;
    }

    public static class InsuranceV2{
        public ArrayList<List> list;
    }

    public static class List{
        public String tab;
        public String tabIcon;
        public String name;
        public String insuranceType;
        public String productCode;
        public String icon;
        public String currency;
        public double price;
        public double basePrice;
        public double discountPercentage;
        public String label;
        public ArrayList<BenefitList> benefitList;
        public String description;
        public String policyUrl;
        public ArrayList<Compensation> compensations;
        public String longName;
        public String subTitle;
        public String credentialCode;
        public boolean isSelected;
    }

    public static class Location{
        public Coordinates coordinates;
    }

    public static class MainFacility{
        public String name;
        public String icon;
    }

    public static class MainImage{
        public String caption;
        public String url;
        public String mobileUrl;
        public Object orderNumber;
    }

    public static class PaymentOptionList{
        public String id;
        public String name;
        public String subName;
        public String disclaimer;
        public String description;
        public String icon;
    }

    public static class Policy{
        public double charge;
        public Date deadline;
        public double percentage;
        public boolean isNoShowPolicy;
        public String icon;
        public String title;
        public String type;
    }

    public static class PolicyToShow{
        public String icon;
        public String textColor;
        public String title;
        public String type;
        public Object description;
        public ArrayList<Policy> policies;
    }

    public static class PreferenceForm{
        public String name;
        public String placeholder;
        public String caption;
        public String title;
        public String type;
        public ArrayList<Validator> validators;
        public Object referenceField;
        public String inputSourceURL;
        public ArrayList<InputSource> inputSources;
        public Object disclaimer;
    }

    public static class Price{
        public double baseRateWithTax;
        public double rateWithTax;
        public double totalBaseRateWithTax;
        public double totalRateWithTax;
        public double rateWithoutTax;
        public double baseRateWithoutTax;
        public double totalRateWithoutTax;
        public double totalBaseRateWithoutTax;
    }

    public static class PricePerNight{
        public String stayingDate;
        public double rate;
        public double baseRate;
        public double loyaltyRate;
        public ArrayList<Discount> discounts;
        public double taxAndOtherFee;
        public Object baseTaxAndOtherFee;
        public Object priceEngineSubsidy;
        public Object additionalFee;
        public double discountTax;
        public Object priceAdjustment;
        public double taxPercentage;
    }

    public static class PriceSummary{
        public double total;
        public double totalWithoutTax;
        public double taxAndOtherFee;
        public double totalCompulsory;
        public double totalSellingRateAddOn;
        public double totalInsurance;
        public double taxFeeRoom;
        public double originalPrice;
        public ArrayList<Surcharge> surcharge;
        public Object compulsory;
        public ArrayList<PricePerNight> pricePerNight;
        public TotalObject totalObject;
        public Object internalBreakdown;
        public double discountTax;
        public Object totalPay;
    }

    public static class Promo{
        public String promoText;
        public boolean packageDeals;
        public boolean memberDeals;
        public boolean tonightDeals;
        public String promoIcon;
        public String promoLabelId;
        public int promoPriority;
    }

    public static class RateInfo{
        public String currency;
        public double sellRate;
        public Object localCurrency;
        public boolean refundable;
        public Price price;
        public TixPoint tixPoint;
        public PriceSummary priceSummary;
        public Object promotions;
    }

    public static class Region{
        public String id;
        public String name;
    }

    public static class Room{
        public String roomId;
        public String rateCode;
        public RoomType roomType;
        public RateInfo rateInfo;
        public Promo promo;
        public boolean soldOut;
        public String roomName;
        public MainImage mainImage;
        public ArrayList<Object> images;
        public String descriptionRoom;
        public ArrayList<BenefitAdded> benefitAdded;
        public int maxOccupancy;
        public String roomSize;
        public String bedType;
        public ArrayList<FeatureAdded> featureAdded;
        public ArrayList<MainFacility> mainFacilities;
        public boolean freeCancellation;
        public String cancellationPolicy;
        public CancellationPolicyInfo cancellationPolicyInfo;
        public Object cancellationPolicies;
        public ArrayList<CancellationPoliciesV2> cancellationPoliciesV2;
        public CancellationPoliciesV3 cancellationPoliciesV3;
        public String paymentOption;
        public Object checkInInstruction;
        public Object specialCheckInInstructions;
        public ArrayList<GroupFacility> groupFacilities;
        public Object payUponArrival;
        public ArrayList<SmokingPreference> smokingPreferences;
        public Object childrenPolicy;
        public int maxChildOccupancy;
        public Object valueAdded;
        public ArrayList<BedType> bedTypes;
        public String cartId;
        public int numberOfRooms;
        public ArrayList<PreferenceForm> preferenceForm;
        public String surchargeInformation;
        public String rateKeyMapping;
        public SelectedRateKey selectedRateKey;
    }

    public static class RoomType{
        public String id;
        public String name;
    }

    public static class SelectedRateKey{
        public Object rateKeyMapping;
        public ArrayList<String> ratePlanTypes;
    }

    public static class SmokingPreference{
        public String id;
        public String name;
    }

    public static class Surcharge{
        public String type;
        public String name;
        public double rate;
        public Object chargeUnit;
        public Object code;
    }

    public static class Tier{
        public String key;
        public String value;
    }

    public static class TixPoint{
        public int value;
        public double worth;
    }

    public static class TotalObject{
        public String label;
        public double value;
    }

    public static class Validator{
        public String name;
        public String message;
        public String parameter;
    }

    public static class ValueRange{
        public String start;
        public String end;
        public String value;
    }
}