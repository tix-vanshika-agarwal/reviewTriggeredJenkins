package com.tiket.pojo.room;

import java.util.ArrayList;
import java.util.Date;

public class SelectRoomResponseV2{

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class BenefitAdded{
        public String icon;
        public String name;
        public String type;
        public boolean showInList;
    }

    public static class CancellationPoliciesV2{
        public double amount;
        public Date time;
    }

    public static class CancellationPoliciesV3{
        public String policyContent;
        public String additionalItems;
        public ArrayList<Policy> policies;
    }

    public static class Data{
        public String hotelAnnouncement;
        public ArrayList<RoomList> roomList;
        public ArrayList<Object> alternateRoomList;
        public ArrayList<PaymentOptionList> paymentOptionList;
        public Object loyaltyInfo;
        public ArrayList<Object> childAges;
        public ArrayList<FilterSummary> filterSummary;
        public ArrayList<Usp> usp;
    }

    public static class FeatureAdded{
        public String icon;
        public String name;
        public String type;
        public boolean showInList;
        public boolean showInDetail;
        public boolean available;
    }

    public static class FilterSummary{
        public String key;
        public String name;
    }

    public static class GroupFacility{
        public String name;
        public String icon;
        public ArrayList<String> detail;
    }

    public static class MainFacility{
        public String name;
        public String icon;
    }

    public static class MainImage{
        public String caption;
        public Object url;
        public Object mobileUrl;
        public Object orderNumber;
    }

    public static class PaymentOptionList{
        public String id;
        public String name;
        public String subName;
        public String description;
        public String icon;
        public String disclaimer;
    }

    public static class Policy{
        public double charge;
        public Date deadline;
        public double percentage;
        public boolean isNoShowPolicy;
    }

    public static class Price{
        public double baseRateWithTax;
        public double rateWithTax;
        public double totalBaseRateWithTax;
        public double totalRateWithTax;
        public double rateWithoutTax;
        public double baseRateWithoutTax;
        public double totalBaseRateWithoutTax;
        public double totalRateWithoutTax;

    }

    public static class PricePerNight{
        public String stayingDate;
        public double rate;
        public double loyaltyRate;
        public double baseRate;
        public Object discounts;
        public double taxAndOtherFee;
        public double priceEngineSubsidy;
    }

    public static class PriceSummary{
        public double total;
        public double totalWithoutTax;
        public double taxAndOtherFee;
        public double totalCompulsory;
        public double totalSellingRateAddOn;
        public double totalInsurance;
        public double originalPrice;
        public ArrayList<Surcharge> surcharge;
        public Object compulsory;
        public ArrayList<PricePerNight> pricePerNight;
        public TotalObject totalObject;
    }

    public static class Promo{
        public String promoText;
        public boolean packageDeals;
        public boolean memberDeals;
        public boolean tonightDeals;
        public String promoIcon;
        public String promoLabelId;
        public Object expiredDate;
        public String idColorLabel;
    }

    public static class RateInfo{
        public String currency;
        public Object localCurrency;
        public boolean refundable;
        public Price price;
        public TixPoint tixPoint;
        public PriceSummary priceSummary;
        public Object loyaltyMembersDeal;
    }

    public static class Room{
        public String roomId;
        public String roomName;
        public String descriptionRoom;
        public int maxOccupancy;
        public int availableRoom;
        public String bedType;
        public String roomSize;
        public String cancellationPolicy;
        public Object cancellationPolicies;
        public ArrayList<CancellationPoliciesV2> cancellationPoliciesV2;
        public CancellationPoliciesV3 cancellationPoliciesV3;
        public String checkInInstruction;
        public String specialCheckInInstructions;
        public String rateCode;
        public ArrayList<Object> payUponArrival;
        public RateInfo rateInfo;
        public Promo promo;
        public ArrayList<String> valueAdded;
        public MainImage mainImage;
        public ArrayList<Object> images;
        public String itemColor;
        public XInfo xInfo;
        public ArrayList<MainFacility> mainFacilities;
        public ArrayList<GroupFacility> groupFacilities;
        public String paymentOption;
        public ArrayList<SmokingPreference> smokingPreferences;
        public RoomType roomType;
        public ArrayList<FeatureAdded> featureAdded;
        public ArrayList<BenefitAdded> benefitAdded;
        public ArrayList<RoomFilter> roomFilter;
        public String childrenPolicy;
        public int maxChildOccupancy;
        public Object alternateAdult;
        public Object alternateRoomNumber;
        public Object alternateChild;
        public ExclusiveNudges exclusiveNudges;
        public FacilityNudges facilityNudges;
    }

    public static class ExclusiveNudges{
        public String title;
        public String iconUrl;
        public String textColor;
        public String backgroundColor;
        public boolean outline;
    }

    public static class FacilityNudges{
        public String title;
        public String iconUrl;
        public String textColor;
        public String backgroundColor;
        public boolean outline;
    }

    public static class RoomFilter{
        public String key;
        public String name;
        public boolean value;
    }

    public static class RoomList{
        public String name;
        public Object groupingKey;
        public String groupDesign;
        public Object groupingVariablesInfo;
        public ArrayList<Object> images;
        public ArrayList<Room> rooms;
    }

    public static class RoomType{
        public String id;
        public String name;
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

    public static class TixPoint{
        public int value;
        public double worth;
    }

    public static class TotalObject{
        public String label;
        public double value;
    }

    public static class Usp{
        public String id;
        public String title;
        public String subTitle;
        public String icon;
    }

    public static class XInfo{
        public String roomExternalId;
        public String hotelExternalId;
        public double netPrice;
        public boolean dynamicPricing;
    }
}