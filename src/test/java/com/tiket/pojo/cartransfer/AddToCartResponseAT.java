package com.tiket.pojo.cartransfer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AddToCartResponseAT {
    public String code;
    public String message;
    public String errors;
    public Data data;
    public Long serverTime;

    public static class Data{
        public String cartId;
        public String catalogueItemId;
        public Origin origin;
        public Destination destination;
        public Fleet fleet;
        public String pickupDate;
        public ArrayList<Object> facilities;
        public ArrayList<Object> generalFacilities;
        public Object form;
        public Object optionData;
        public String transferMethod;
        public Object banner;
        public ArrayList<Object> section;
    }

    public static class Origin{
        public String airportId;
        public String iataCode;
        public String name;
        public String address;
        public String city;
        public String country;
        public String boothID;
        public String boothName;
    }

    public static class Destination{
        public String name;
        public String address;
        public String city;
    }

    public static class Fleet{
        public String fleetId;
        public String businessName;
        public String name;
        public BigDecimal totalPrice;
        public double maxBaggage;
        public int maxPassenger;
        public ArrayList<String> images;
        public String description;
    }


}
