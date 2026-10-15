package com.tiket.pojo.cartransfer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class GetCatalogueIdResponseAT {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public String terms;
        public Object policy;
        public ArrayList<Object> facilities;
        public ArrayList<Object> campaigns;
        public ArrayList<Items> items;
        public String productPolicy;
        public String refundPolicy;
        public PickupInstruction pickupInstruction;
        public Object requestParams;
        public Object banner;
        public String message;
    }

    public static class Items{
        public CarType carType;
        public ArrayList<Fleet> fleets;

    }
    public static class CarType{
        public String id;
        public String label;
        public String cardType;
        public String name;
        public String usp;
        public int maxPax;
        public int maxBaggage;
        public String carModel;
        public BigDecimal initialPrice;
        public BigDecimal finalPrice;
        public ArrayList<String> campaigns;
        public ArrayList<String> images;
    }

    public static class Fleet{
        public String catalogueItemId;
        public String fleetId;
        public String businessName;
        public String businessLogoUrl;
        public String name;
        public String description;
        public double tixPoint;
        public long priority;
        public double initialPrice;
        public double finalPrice;
        public String carType;
        public ArrayList<String> campaigns;
        public ArrayList<String> facilities;
        public Rating rating;
        public boolean bpg;
    }

    public static class Rating{
        public String value;
        public String max;
        public String color;
    }

    public static class PickupInstruction{
        public String title;
        public String enclosingTag;
        public ArrayList<Item> items;
    }
    public static class Item{
        public String image;
        public String detail;
    }
}
