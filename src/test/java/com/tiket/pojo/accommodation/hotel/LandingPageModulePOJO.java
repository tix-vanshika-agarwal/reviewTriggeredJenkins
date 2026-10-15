package com.tiket.pojo.accommodation.hotel;

import java.math.BigInteger;
import java.util.ArrayList;

public class LandingPageModulePOJO {
    public String code;
    public String message;
    public Data data;
    public Object errors;
    public BigInteger serverTime;

    public static class Data{
        public String templateCode;
        public CurrencyData currencyData;
        public ArrayList<List> list;

        //TIMED_PROMO
        public String icon;
        public String topColor;
        public String bottomColor;
        public String supergraphicImage;
        public String desktopSuperGraphic;
        public String subsidyLabel;
        public BigInteger remainingTime;

    }

    public static class CurrencyData {
        public String code;
        public int scale;
    }

    public static class List{
        //MULTIPLE_SQUARE_CARD
        public String icon;
        public String titleContent;
        public String label;

        //MULTIPLE_HOTEL_ID //TIMED_PROMO
        public String location;
        public String buttonText;
        public ArrayList<Hotel> hotels;

        public String redirectTarget;
        public Redirect redirect;
    }

    public static class Destination{
        public String type;
        public String publicId;
        public String name;
        public String location;
        public Country country;
        public String labelText;
    }

    public static class Image{
        public String caption;
        public String url;
        public String mobileUrl;
    }

    public static class Module{
        public String name;
        public String title;
        public Image image;
    }

    public static class PopularDestination{
        public ArrayList<Destination> destinations;
    }

    public static class Redirect{
        public String location;
        public ArrayList<String> facilityGroups;
        public String priorityRanking;
        public ArrayList<String> accommodationType;
        public ArrayList<String> subsidyLabels;
        public Object hotelBrands;
        public ArrayList<Object> hotelChains;
        public Object maxPrice;
        public Object minPrice;
        public Object roomFacilityTags;
        public Object starRatings;
        public Object paymentOptions;
        public boolean tiketFlexi;
        public String locationType;
        public String groupFilterKeyword;
        public boolean loyaltyToggle;
        public Object reviewOptions;
        public Object benefits;
        public boolean preferredPartner;
        public PopularDestination popularDestination;
        public Object autocompleteLocation;
        public Object poi;
        public ArrayList<String> srpPropertyTypes;
        public ArrayList<String> recentPoiTypes;
        public ArrayList<String> recentAutocompleteLocation;
        public boolean showSearchBox;
        public String button;
    }

    public static class Hotel{
        public String hotelId;
        public String name;
        public MainImage mainImage;
        public double starRating;
        public Country country;
        public Region region;
        public City city;
        public Area area;
        public Reviews reviews;
        public RateInfo rateInfo;
        public String label;
        public boolean soldOut;
        public String propertyType;
        public PreferredPartner preferredPartner;
        public RoomStatistic roomStatistic;
        public LoyaltyMembersDeal loyaltyMembersDeal;
        public Category category;
        public String accommodationType;
    }

    public static class MainImage{
        public String caption;
        public String url;
        public String mobileUrl;
    }

    public static class Country{
        public String id;
        public String name;
    }

    public static class Region{
        public String id;
        public String name;
    }

    public static class City{
        public String id;
        public String name;
    }

    public static class Area{
        public String id;
        public String name;
    }

    public static class Reviews{
        public double score;
        public Tiket tiket;
        public TripAdvisor tripAdvisor;
    }

    public static class Tiket{
        public double rating;
        public int count;
        public String impression;
        public String url;
        public String ratingImageUrl;
    }

    public static class TripAdvisor{
        public double rating;
        public int count;
        public String impression;
        public String url;
        public String ratingImageUrl;
    }

    public static class RateInfo{
        public String currency;
        public int scale;
        public Price price;
        public PriceSummary priceSummary;
    }

    public static class Price{
        public int baseRateWithTax;
        public int rateWithTax;
        public int totalBaseRateWithTax;
        public int totalRateWithTax;
    }

    public static class PriceSummary{
        public int totalWithoutTax;
        public int taxAndOtherFee;
        public int total;
        public ArrayList<Object> compulsory;
        public Object pricePerNight;
        public TotalObject totalObject;
    }

    public static class TotalObject{
        public String label;
        public int value;
    }

    public static class PreferredPartner{
        public String tier;
        public String icon;
    }

    public static class RoomStatistic{
        public int maximumCapacity;
        public String roomSize;
        public int numberOfBedroom;
    }

    public static class LoyaltyMembersDeal{
        public String key;
        public String value;
        public String iconUrl;
    }

    public static class Category{
        public String id;
        public String name;
    }
}
