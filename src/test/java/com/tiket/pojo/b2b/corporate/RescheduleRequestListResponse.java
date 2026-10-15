package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class RescheduleRequestListResponse {
    public boolean success;
    public List<Data> data;
    public Object metadata;

    public static class Data {
        public int id;
        public String type;
        public Amount additionalAmount;
        public Amount cashbackAmount;
        public String productType;
        public String updatedBy;
        public Employee employee;
        public String status;
        public String notes;
        public String purchaseDate;
        public String createdAt;
        public String expiredAt;
        public String orderNotes;
        public Order oldOrder;
        public Order newOrder;
        public boolean approvable;
    }

    public static class Amount {
        public String amount;
        public String point;
        public String paymentMethod;
    }

    public static class Employee {
        public String employeeDivision;
        public String employeeEmail;
        public String employeeName;
        public String employeeApproverEmail;
    }

    public static class Order {
        public Orders orders;
    }

    public static class Orders {
        public String orderHash;
        public String orderStatus;
        public int orderID;
        public String amount;
        public String priceWithoutRescheduleFee;
        public OrderDetail orderDetail;
        public OrderDetail oldOrderDetail;
        public List<Traveller> travellers;
        public Contact contact;
        public String totalAmount;
    }

    public static class OrderDetail {
        public int orderDetailId;
        public String hotelName;
        public int guest;
        public String checkinDate;
        public String checkoutDate;
        public int quantity;
        public int duration;
        public HotelDetail hotelDetail;
    }

    public static class HotelDetail {
        public String typeRoom;
        public int roomNumber;
        public String status;
        public Object specialRequest;
        public int starRating;
    }

    public static class Traveller {
        public String salutation;
        public String fullName;
        public Vertical vertical;
    }

    public static class Vertical {
        public String busSeat;
    }

    public static class Contact {
        public String email;
        public int accountId;
        public String phoneNumber;
        public String contactName;
        public String title;
    }
}
