package com.tiket.pojo.accommodation.hotel;

import java.util.ArrayList;

public class SubmitBookingV2Body {

    public ContactPerson contactPerson;
    public ArrayList<Hotel> hotel;
    public ArrayList<Object> insurances;
    public ArrayList<Object> insurancesCredentials;

    public static class Books {
        public String bedTypePreference;
        public ArrayList<BookingAddOnRequests> bookingAddOnRequests;
        public String cartId;
        public String checkIn;
        public String checkOut;
        public ArrayList<Object> childAges;
        public String connectingRoom;
        public String highFloorRoom;
        public ArrayList<Room> rooms;
        public String smokingPreference;
        public String specialRequest;
    }

    public static class BookingAddOnRequests {
        public ArrayList<Addons> addons;
        public String date;
    }

    public static class Addons {
        public int amount;
        public String id;
        public String note;
    }

    public static class ContactPerson {
        public String areaCode;
        public String email;
        public String fullName;
        public String phone;
        public String profileId;
        public String title;
    }

    public static class GuestDetail {
        public String fullName;
        public String profileId;
        public String title;
    }

    public static class Hotel {
        public ArrayList<Books> books;
        public String hotelId;
    }

    public static class Room {
        public GuestDetail guestDetail;
        public ArrayList<Object> nights;
    }
}
