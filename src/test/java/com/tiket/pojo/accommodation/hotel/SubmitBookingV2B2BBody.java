package com.tiket.pojo.accommodation.hotel;

import java.util.ArrayList;

public class SubmitBookingV2B2BBody {
    public ContactPerson contactPerson;
    public ArrayList<Object> insurances;
    public ArrayList<Object> insurancesCredential;
    public ArrayList<Hotel> hotel;
    public Object crossSellingSource;
    public Object traveller;
    public int previousOrderId;
    public B2bInterimPageData b2bInterimPageData;

    public static class B2bInterimPageData{
        public String corporateInterimID;
        public String corporatePaymentPreference;
        public String serviceFee;
        public String serviceFeeType;
        public String transactionalStatus;
    }

    public static class Book{
        public String cartId;
        public ArrayList<Object> bookingAddOnRequests;
        public ArrayList<Object> childAges;
        public ArrayList<Room> rooms;
    }

    public static class ContactPerson{
        public long profileId;
        public String title;
        public String fullName;
        public String email;
        public String phone;
        public String areaCode;
        public String id;
        public String countryId;
    }

    public static class GuestDetail{
        public long profileId;
        public String title;
        public String fullName;
        public String nationalityCode;
    }

    public static class Hotel{
        public String hotelId;
        public ArrayList<Book> books;
    }

    public static class Room{
        public GuestDetail guestDetail;
        public ArrayList<Object> nights;
    }
}
