package com.tiket.pojo.accommodation.hotel;

import java.util.List;

public class HotelApprovalApprovedResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public List<Content> content;
        public Pageable pageable;
        public boolean last;
        public int totalElements;
        public int totalPages;
        public boolean first;
        public int size;
        public int number;
        public Sort sort;
        public int numberOfElements;
        public boolean empty;
    }

    public static class Content {
        public Hotel hotel;
        public String creationDate;
        public PaymentInformation paymentInformation;
        public HotelCommission hotelCommission;
        public String lastApprovedDate;
        public String submittedDate;
        public List<Pic> picList;
        public TiketEntity tiketEntity;
        public EntityCommission entityCommission;
    }

    public static class Hotel {
        public int id;
        public int version;
        public HotelGeneralInformation hotelGeneralInformation;
        public int activeStatus;
        public String currency;
        public Object location;
        public Object entity;
        public String contractUrl;
        public Object vendor;
        public String accommodationType;
        public String registerFormType;
        public boolean newProperty;
        public String rejectReason;
        public Object isInvitedLoyalty;
        public boolean occupancyBasedRate;
        public String referralCode;
        public Object placeType;
        public Object channelSourceId;
        public Object channelSourceValue;
        public Object vendorSkipPriceValidation;
        public String inventoryLevel;
        public Object ignoreVendorContent;
        public Object picAccountIds;
        public Object canOverrideTax;
        public Object ignoreMasterTax;
    }

    public static class HotelGeneralInformation {
        public String name;
        public PropertyType propertyType;
        public Country country;
        public Region region;
        public City city;
        public Area area;
        public String rateModel;
    }

    public static class PropertyType {
        public int id;
        public Object version;
        public Object accommodationType;
        public Object isUnit;
        public Object isEntirePlace;
    }

    public static class Country {
        public Object id;
        public Object version;
        public String name;
        public Object mmEmail;
        public Object commissionValue;
        public Object commissionType;
        public Object phoneCode;
        public Object currencyCode;
    }

    public static class Region {
        public Object id;
        public Object version;
        public String name;
        public double taxPercentage;
        public Object mmEmail;
    }

    public static class City {
        public Object id;
        public Object version;
        public String name;
        public Object mmEmail;
    }

    public static class Area {
        public Object id;
        public Object version;
        public String name;
    }

    public static class PaymentInformation {
        public String paymentMethod;
        public Object paymentPlan;
        public Object bank;
        public String currency;
        public Object bankOther;
        public Object vccUsageDaysStart;
        public Object payoutCurrency;
        public boolean swipeVccNonRefundableImmediately;
        public Object vccMccRegistered;
        public boolean hasTaxAccount;
    }

    public static class HotelCommission {
        public String tariffMode;
        public double percentage;
        public double serviceFee;
        public Object isExclusivePartner;
        public String commissionFrom;
    }

    public static class Pic {
        public int accountId;
        public String email;
    }

    public static class TiketEntity {
        public int id;
        public int version;
        public String name;
        public Country country;
        public Object vccTemplateId;
        public Object emailFooterImageUrl;
    }

    public static class EntityCommission {
        public int id;
        public int version;
        public TiketEntity entity;
        public double commissionPercentage;
        public TiketEntity entityPair;
        public double commissionPercentagePair;
    }

    public static class Pageable {
        public int pageNumber;
        public int pageSize;
        public Sort sort;
        public int offset;
        public boolean unpaged;
        public boolean paged;
    }

    public static class Sort {
        public boolean empty;
        public boolean sorted;
        public boolean unsorted;
    }
}
