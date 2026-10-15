package com.tiket.pojo.accommodation.hotel;

import java.util.ArrayList;

public class PreBookMultiRoomBody {

    public ArrayList<Hotel> hotels;
    public boolean isMultiOrder;

    public static class Hotel{
        public int adult;
        public ArrayList<Object> childAges;
        public String hotelId;
        public int night;
        public String startDate;
        public ArrayList<Room> rooms;
    }

    public static class Room{
        public int maxChildOccupancy;
        public int maxOccupancy;
        public int qty;
        public String rateCode;
        public String roomId;
    }
}
