package com.tiket.pojo.accommodation.hotel;

import java.math.BigInteger;
import java.util.ArrayList;

public class PreBookGeneratingCartBody {
    public boolean isMultiOrder;
    public ArrayList<Hotel> hotels;
    public String urlReferer;
    public String transactionStatus;


    public static class Hotel{
        public int adult;
        public ArrayList<Object> childAges;
        public String hotelId;
        public int night;
        public ArrayList<Room> rooms;
        public String startDate;
        public String endDate;
        public ArrayList<AddOnCategoryGroup> addOnCategoryGroups;
    }

    public static class Room{
        public String roomId;
        public String rateCode;
        public int qty;
        public int maxOccupancy;
        public int maxChildOccupancy;
    }

    public static class AddOnCategoryGroup {
        public String name;
        public String description;
        public ArrayList<AddOn> addOns;
        public String category;
    }

    public static class AddOn {
        public String id;
        public String name;
        public String type;
        public int minAmount;
        public int maxAmount;
        public RateInfo rateInfo;
    }

    public static class RateInfo {
        public String currency;
        public BigInteger sellRate;
    }
}
