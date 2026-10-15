package com.tiket.pojo.accommodation.hotel;

import java.util.ArrayList;
import java.util.Date;

public class PreBookMultiRoomResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;
    public boolean isSalutationEnabled;

    public static class AddOnCategoryGroup {
        public static class AddOn {
            public int id;
            public String name;
            public String description;
            public String type;
            public String imageUrl;
            public String thumbnailUrl;
            public int minAmount;
            public int maxAmount;
            public RateInfo rateInfo;
            public ArrayList < AddOnsForm > addOnsForm;
            public double tixPoint;
            public ArrayList < String > availableRoomIdentifier;
        }

        public String name;
        public String iconUrl;
        public String info;
        public String description;
        public ArrayList < AddOn > addOns;
        public String category;
    }

    public static class AddOnsForm {
        public String name;
        public String placeholder;
        public String caption;
        public String title;
        public String type;
        public ArrayList < Validator > validators;
        public String referenceField;
        public String inputSourceURL;
        public ArrayList < Object > inputSources;
        public Object disclaimer;
    }

    public static class Area {
        public String id;
        public String name;
    }

    public static class BedType {
        public String roomCategory;
        public String bedType;
    }

    public static class BenefitAdded {
        public String icon;
        public String name;
    }

    public static class Breakdown {
        public String title;
        public double value;
        public String type;
    }

    public static class CancellationPoliciesV2 {
        public double amount;
        public Date time;
    }

    public class CancellationPoliciesV3 {
        public String policyContent;
        public Object additionalItems;
        public ArrayList < Policy > policies;
        public PolicyToShow policyToShow;
        public boolean refundable;
    }

    public static class CancellationPolicyInfo {
        public String title;
        public String subTitle;
        public String icon;
    }

    public static class City {
        public String id;
        public String name;
    }

    public static class Coordinates {
        public double latitude;
        public double longitude;
    }

    public static class Country {
        public String id;
        public String name;
    }

    public static class CurrencyData {
        public String code;
        public int scale;
    }

    public class Data {
        public CurrencyData currencyData;
        public ArrayList < Hotel > hotels;
        public DictionaryImages dictionaryImages;
        public Object insuranceV2;
        public Object freeProtection;
        public ArrayList < Object > surcharge;
        public PromoBreakdown promoBreakdown;
        public Object footerBanner;
        public Object popup;
    }

    public class Detail {
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
        public ArrayList < Room > rooms;
        public Object placeType;
    }

    public static class DetailArray {
        public String code;
        public String title;
        public String subTitle;
        public Object icon;
        public String description;
    }

    public static class DictionaryImages {
        public String bgBenefitBanner;
        public String icBenefitBanner;
        public String supergraphicBlue;
        public String bgBenefitForYou;
        public String supergraphicGradientGreen;
        public String supergraphicGradientYellow;
    }

    public static class Discount {
        public String type;
        public String promoText;
        public double value;
    }

    public static class ExtraBenefits {
        public double value;
        public double percentWorth;
        public ArrayList < DetailArray > details;
        public String summary;
    }

    public static class FeatureAdded {
        public String icon;
        public String name;
        public String type;
        public boolean available;
    }

    public static class GeneralInfo {
        public String checkIn;
        public String checkOut;
        public String policy;
    }

    public static class GroupFacility {
        public String name;
        public String icon;
        public ArrayList < String > detail;
    }

    public class Hotel {
        public String startDate;
        public String endDate;
        public int night;
        public ArrayList < AddOnCategoryGroup > addOnCategoryGroups;
        public ArrayList < PaymentOptionList > paymentOptionList;
        public Object taxDescription;
        public Detail detail;
        public Object giftVouchers;
    }

    public static class Image {
        public String caption;
        public String url;
        public String mobileUrl;
        public int orderNumber;
    }

    public static class InputSource {
        public String name;
        public String label;
        public String value;
        public Object image;
        public Object price;
        public Object tixPoint;
        public ValueRange valueRange;
    }

    public static class Location {
        public Coordinates coordinates;
    }

    public static class LoyaltyMembersDeal {
        public String name;
        public double totalValue;
        public double totalPercentWorth;
        public String iconUrl;
        public double baseValue;
        public double basePercentWorth;
        public Tier tier;
        public ExtraBenefits extraBenefits;
        public Object blackoutDates;
        public Object blackoutDisclaimer;
        public String backgroundColor;
        public String borderBgColor;
        public String title;
    }

    public static class MainFacility {
        public String name;
        public String icon;
    }

    public static class MainImage {
        public String caption;
        public String url;
        public String mobileUrl;
        public Object orderNumber;
    }

    public static class PaymentOptionList {
        public String id;
        public String name;
        public String subName;
        public String disclaimer;
        public String description;
        public String icon;
    }

    public static class Policy {
        public double charge;
        public Date deadline;
        public double percentage;
        public boolean isNoShowPolicy;
        public String icon;
        public String title;
        public String type;
    }

    public static class PolicyToShow {
        public String icon;
        public String textColor;
        public String title;
        public String type;
        public Object description;
        public ArrayList < Policy > policies;
    }

    public static class PreferenceForm {
        public String name;
        public String placeholder;
        public String caption;
        public String title;
        public String type;
        public ArrayList < Validator > validators;
        public Object referenceField;
        public String inputSourceURL;
        public ArrayList < InputSource > inputSources;
        public Object disclaimer;
    }

    public static class Price {
        public double baseRateWithTax;
        public double rateWithTax;
        public double totalBaseRateWithTax;
        public double totalRateWithTax;
        public double rateWithoutTax;
        public double baseRateWithoutTax;
        public double totalRateWithoutTax;
        public double totalBaseRateWithoutTax;
    }

    public static class PricePerNight {
        public String stayingDate;
        public double rate;
        public double baseRate;
        public double loyaltyRate;
        public ArrayList < Discount > discounts;
        public double taxAndOtherFee;
        public Object baseTaxAndOtherFee;
        public Object priceEngineSubsidy;
        public Object additionalFee;
    }

    public static class PriceSummary {
        public double total;
        public double totalWithoutTax;
        public double taxAndOtherFee;
        public double totalCompulsory;
        public double totalSellingRateAddOn;
        public double totalInsurance;
        public double taxFeeRoom;
        public double originalPrice;
        public ArrayList < Object > surcharge;
        public Object compulsory;
        public ArrayList < PricePerNight > pricePerNight;
        public TotalObject totalObject;
        public Object internalBreakdown;
        public Object totalPay;
    }

    public static class Promo {
        public String promoText;
        public boolean packageDeals;
        public boolean memberDeals;
        public boolean tonightDeals;
        public String promoIcon;
        public String promoLabelId;
        public int promoPriority;
    }

    public class PromoBreakdown {
        public double totalValue;
        public String header;
        public ArrayList < Breakdown > breakdowns;
        public ArrayList < Detail > details;
    }

    public static class RateInfo {
        public String currency;
        public double sellRate;
        public Object localCurrency;
        public boolean refundable;
        public Price price;
        public TixPoint tixPoint;
        public PriceSummary priceSummary;
        public LoyaltyMembersDeal loyaltyMembersDeal;
        public Object promotions;
    }

    public static class Region {
        public String id;
        public String name;
    }

    public class Room {
        public String roomId;
        public String rateCode;
        public RoomType roomType;
        public RateInfo rateInfo;
        public Promo promo;
        public boolean soldOut;
        public String roomName;
        public MainImage mainImage;
        public ArrayList < Image > images;
        public String descriptionRoom;
        public ArrayList < BenefitAdded > benefitAdded;
        public int maxOccupancy;
        public String roomSize;
        public String bedType;
        public ArrayList < FeatureAdded > featureAdded;
        public ArrayList < MainFacility > mainFacilities;
        public boolean freeCancellation;
        public String cancellationPolicy;
        public CancellationPolicyInfo cancellationPolicyInfo;
        public Object cancellationPolicies;
        public ArrayList < CancellationPoliciesV2 > cancellationPoliciesV2;
        public CancellationPoliciesV3 cancellationPoliciesV3;
        public String paymentOption;
        public Object checkInInstruction;
        public Object specialCheckInInstructions;
        public ArrayList < GroupFacility > groupFacilities;
        public Object payUponArrival;
        public ArrayList < SmokingPreference > smokingPreferences;
        public Object childrenPolicy;
        public int maxChildOccupancy;
        public Object valueAdded;
        public ArrayList < BedType > bedTypes;
        public String cartId;
        public int numberOfRooms;
        public ArrayList < PreferenceForm > preferenceForm;
        public String surchargeInformation;
        public String rateKeyMapping;
        public Object selectedRateKey;
    }

    public static class RoomType {
        public String id;
        public String name;
    }

    public static class SmokingPreference {
        public String id;
        public String name;
    }

    public static class Tier {
        public String key;
        public String value;
    }

    public static class TixPoint {
        public int value;
        public double worth;
    }

    public static class TotalObject {
        public String label;
        public double value;
    }

    public static class Validator {
        public String name;
        public String message;
        public String parameter;
    }

    public static class ValueRange {
        public String start;
        public String end;
        public String value;
    }
}
