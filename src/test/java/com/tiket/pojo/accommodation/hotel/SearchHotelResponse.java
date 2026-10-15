package com.tiket.pojo.accommodation.hotel;

import java.util.ArrayList;

public class SearchHotelResponse {
    public String code;
    public String message;
    public Data data;
    public Object errors;
    public int serverTime;

    public static class Area{
        public String id;
        public String name;
        public String subtitle;
    }

    public static class BeforeDynamicPrice{
        public int baseRateWithTax;
        public int rateWithTax;
        public int totalBaseRateWithTax;
        public int totalRateWithTax;
    }

    public static class BenefitAdded{
        public String id;
        public String name;
        public String iconUrl;
    }

    public static class Category{
        public String id;
        public String name;
        public String subtitle;
    }

    public static class City{
        public String id;
        public String name;
        public String subtitle;
    }

    public static class Content{
        public String itemColor;
        public BeforeDynamicPrice beforeDynamicPrice;
        public int availableRoom;
        public boolean refundable;
        public boolean soldOut;
        public RateInfo rateInfo;
        public Promo promo;
        public ArrayList<String> valueAdded;
        public String paymentOption;
        public boolean isNewHotel;
        public ArrayList<BenefitAdded> benefitAdded;
        public ArrayList<ListAdded> listAdded;
        public LoyaltyMembersDeal loyaltyMembersDeal;
        public String hRO;
        public Object querySource;
        public ArrayList<MainCardBenefit> mainCardBenefits;
        public int tixPoints;
        public boolean isAlternate;
        public String id;
        public String hotelName;
        public String address;
        public String postalCode;
        public float starRating;
        public Country country;
        public Region region;
        public City city;
        public Area area;
        public Location location;
        public Reviews reviews;
        public Category category;
        public float poiDistance;
        public ArrayList<MainFacility> mainFacilities;
        public MainImage mainImage;
        public String description;
        public int price;
        public Labels labels;
    }

    public static class Labels{
        public Ads ads;
    }

    public static class Ads{
        public String text;
        public String textColor;
    }

    public static class Coordinates{
        public double longitude;
        public double latitude;
        public double lon;
        public double lat;
    }

    public static class Country{
        public String id;
        public String name;
        public String subtitle;
    }

    public class Data{
        public ArrayList<Content> contents;
        public Pagination pagination;
        public SearchDetail searchDetail;
        public ArrayList<PaymentOptionList> paymentOptionList;
        public Object bannerList;
    }

    public static class ListAdded{
        public String id;
        public String name;
        public String iconUrl;
        public String subName;
    }

    public static class Location{
        public Coordinates coordinates;
    }

    public static class LoyaltyMembersDeal{
        public String key;
        public String value;
        public String iconUrl;
        public String labelColor;
        public String textColor;
    }

    public static class MainCardBenefit{
        public String text;
        public String colorConfig;
    }

    public static class MainFacility{
        public String name;
        public String icon;
    }

    public static class MainImage{
        public String caption;
        public String url;
        public String mobileUrl;
    }

    public static class NearbyLocation{
        public String type;
        public String id;
        public String name;
    }

    public static class Pagination{
        public int currentPage;
        public int totalDataInPage;
        public int totalPage;
        public int totalData;
    }

    public static class PaymentOptionList{
        public String id;
        public String name;
        public String subName;
        public String description;
        public String icon;
        public String disclaimer;
    }

    public static class PopularSublocation{
        public String id;
        public String type;
        public String name;
    }

    public static class Price{
        public int baseRateWithTax;
        public int rateWithTax;
        public int rateWithoutTax;
        public int totalBaseRateWithTax;
        public int totalRateWithTax;
        public int baseRateWithoutTax;
        public int totalRateWithoutTax;
        public int totalBaseRateWithoutTax;
    }

    public static class PricePerNight{
        public String stayingDate;
        public int rate;
    }

    public static class PriceSummary{
        public int totalWithoutTax;
        public int taxAndOtherFee;
        public int total;
        public ArrayList<Object> compulsory;
        public ArrayList<PricePerNight> pricePerNight;
        public TotalObject totalObject;
    }

    public static class Promo{
        public String promoText;
        public boolean memberDeals;
        public boolean tonightDeals;
        public boolean packageDeals;
        public String promoIcon;
        public Object expiredDate;
        public String promoLabelColor;
    }

    public static class RateInfo{
        public String currency;
        public boolean refundable;
        public Price price;
        public PriceSummary priceSummary;
    }

    public static class Region{
        public String id;
        public String name;
        public String subtitle;
    }

    public static class Reviews{
        public double score;
        public Tiket tiket;
        public Tripadvisor tripadvisor;
    }


    public static class SearchDetail{
        public String searchLocation;
        public Coordinates coordinates;
        public String checkInDate;
        public String checkOutDate;
        public int night;
        public int room;
        public int adult;
        public ArrayList<NearbyLocation> nearbyLocation;
        public Object accommodationType;
        public Object childAges;
        public int maxResultForRecommendation;
        public ArrayList<String> configNearbyRecommendation;
        public String hSV;
        public ArrayList<Variant> variants;
        public boolean showAlternate;
        public ArrayList<PopularSublocation> popularSublocation;
    }

    public static class Tiket{
        public double rating;
        public int count;
        public String impression;
        public String url;
        public String ratingImageUrl;
    }

    public static class TotalObject{
        public String label;
        public int value;
    }

    public static class Tripadvisor{
        public double rating;
        public int count;
        public String impression;
        public String url;
        public String ratingImageUrl;
    }

    public static class Variant{
        public String variantName;
        public int variantID;
        public String experimentName;
        public String experimentType;
        public int experimentID;
        public int cycle;
        public int currentPhase;
    }
}
