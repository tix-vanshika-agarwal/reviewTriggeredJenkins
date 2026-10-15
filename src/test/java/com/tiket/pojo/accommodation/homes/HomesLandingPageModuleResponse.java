package com.tiket.pojo.accommodation.homes;

import java.math.BigInteger;
import java.util.ArrayList;

public class HomesLandingPageModuleResponse {
    public String code;
    public String message;
    public Data data;
    public Object errors;
    public BigInteger serverTime;

    public static class Data{
        public CurrencyData currencyData;
        public Chips chips;
    }

    public static class Chips{
        public String name;
        public String location;
        public String buttonText;
        public ArrayList<Hotel> hotels;
    }

    public static class Hotel{
        public String hotelId;
        public String name;
        public MainImage mainImage;
        public Country country;
        public Region region;
        public City city;
        public Area area;
        public Reviews reviews;
        public RateInfo rateInfo;
        public String label;
        public boolean soldOut;
        public String propertyType;
        public Object preferredPartner;
        public RoomStatistic roomStatistic;
        public LoyaltyMembersDeal loyaltyMembersDeal;
        public Category category;
        public String accommodationType;
    }


    public static class Area{
        public String id;
        public String name;
    }

    public static class Category{
        public String id;
        public String name;
    }



    public static class City{
        public String id;
        public String name;
    }

    public static class Country{
        public String id;
        public String name;
    }

    public static class CurrencyData{
        public String code;
        public int scale;
    }

    public static class LoyaltyMembersDeal{
        public String key;
        public String value;
        public String iconUrl;
    }

    public static class MainImage{
        public String caption;
        public String url;
        public String mobileUrl;
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

    public static class RateInfo{
        public String currency;
        public int scale;
        public Price price;
        public PriceSummary priceSummary;
    }

    public static class Region{
        public String id;
        public String name;
    }

    public static class Reviews{
        public double score;
        public Tiket tiket;
        public TripAdvisor tripAdvisor;
    }

    public static class RoomStatistic{
        public int maximumCapacity;
        public String roomSize;
        public int numberOfBedroom;
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

    public static class TripAdvisor{
        public double rating;
        public int count;
        public String impression;
        public String url;
        public String ratingImageUrl;
    }
}
