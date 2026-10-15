package com.tiket.pojo.order;

import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail.Payment;

import java.util.ArrayList;

public class HotelInternalOrderDetail {
    public String orderId;
    public String orderHash;
    public Data data;

    public static class Data{
        public String orderStatus;
        public ArrayList<OrderDetails> orderDetails;
        public ArrayList<Travelers> travelers;
        public ArrayList<Payment> payments;
        public Contact contact;
    }

    public static class OrderDetails{
        public String orderName;
        public String status;
        public String type;
        public Vertical vertical;
        public String startTimestampTimezone;
        public long orderDetailId;
        public String orderNameDetail;
    }

    public static class Vertical{
        public String hotelId;
        public String hotelItineraryNumber;
        public String supplierItineraryId;
        public String roomName;
        public int bookingNight;
        public CancellationPolicyV3 cancellationPolicyV3;
    }

    public static class CancellationPolicyV3{
        public boolean refundable;
    }

    public static class Travelers{
        public String salutation;
        public String fullName;
        public String firstName;
        public String lastName;
    }

    public static class Contact{
        public String email;
        public String contactName;
        public String phoneNumber;
        public int accountId;
    }
}
