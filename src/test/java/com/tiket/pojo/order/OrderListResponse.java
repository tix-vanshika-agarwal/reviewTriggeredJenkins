package com.tiket.pojo.order;

import java.util.ArrayList;

public class OrderListResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class AdditionalInfo{
        public Object train;
        public Hotel hotel;
        public Object car;
        public Object railink;
    }

    public static class Cancellation{
        public AdditionalInfo additionalInfo;
        public Object giftVoucher;
        public Object tixPoint;
        public boolean enabled;
    }

    public static class Content{
        public int orderId;
        public int accountId;
        public String orderType;
        public String orderHash;
        public String orderDate;
        public String expiredDate;
        public String expiredDateCountdown;
        public boolean orderGroup;
        public Object orderCar;
        public OrderHotel orderHotel;
        public Object orderTrain;
        public OrderFlight orderFlight;
        public Object orderEvent;
        public Object orderAirportTransfer;
        public Object orderRailink;
        public String orderStatus;
        public Object refundOrders;
        public Object rescheduleOrder;
        public int expiredCountDown;
        public String paymentUrl;
        public String paymentTitle;
        public Object eticketUrl;
        public String receiptUrl;
        public String additionalInformation;
        public ArrayList<Object> shareEticketList;
        public String shareReceipt;
        public Cancellation cancellation;
        public int eticketVersion;
        public Object refundReasonOrderStatus;
        public Object rescheduleReasonOrderStatus;
        public ReviseBookingDetail reviseBookingDetail;
        public String urlReschedule;
        public Object urlRescheduleNonRefundable;
        public ArrayList<EticketList> eticketList;
        public Receipt receipt;
        public boolean cancellable;
        public boolean enableCancelInsurance;
        public boolean enableOnlineCheckin;
        public boolean refundable;
        public boolean reschedulable;
        public boolean continuePayment;
        public boolean enableRevise;
        public boolean visibleReschedule;
        public boolean enableShareReceipt;
        public boolean enableShareEticket;
        public boolean deletable;
        public boolean isFlexiTiket;
    }

    public static class Data{
        public ArrayList<Content> content;
        public boolean last;
        public int totalElements;
        public int totalPages;
        public ArrayList<Sort> sort;
        public boolean first;
        public int numberOfElements;
        public int size;
        public int number;
    }

    public static class EticketList{
        public Object url;
        public String filename;
        public int version;
    }

    public static class Hotel{
        public String hotelImage;
    }

    public static class OrderHotel{
        public int orderDetailId;
        public String hotelName;
        public String roomName;
        public int guest;
        public String checkinDate;
        public String checkoutDate;
        public int quantity;
        public int duration;
        public double totalAmount;
        public String customerCurrency;
        public String paymentType;
        public PaymentTypeInfo paymentTypeInfo;
        public String subType;
        public Object adult;
        public Object child;
    }

    public static class PaymentTypeInfo{
        public String id;
        public String name;
        public String subName;
        public String description;
        public String icon;
        public String disclaimer;
    }

    public static class Receipt{
        public String url;
        public String filename;
        public int version;
    }

    public static class ReviseBookingDetail{
        public boolean specialRequestReversed;
        public boolean guestInfoReversed;
        public String subtitleSpecialRequestReversed;
        public String subtitleGuestInfoReversed;
    }

    public static class Sort{
        public String direction;
        public String property;
        public boolean ignoreCase;
        public String nullHandling;
        public boolean ascending;
        public boolean descending;
    }

    public static class OrderFlight{
        public String departureTime;
        public String arrivalCityCode;
        public boolean enableOnlineCheckinDepart;
        public Object boardingPassNameDepart;
        public String returnDate;
        public int departureOrderDetailId;
        public Object urlOnlineCheckinListReturn;
        public String departureCityCode;
        public int departureTimezone;
        public Object boardingPassGeneratedIdDepart;
        public String departureDate;
        public int infantTotal;
        public Object statusOnlineCheckin;
        public int childTotal;
        public boolean roundTrip;
        public Object boardingPassNameReturn;
        public Object boardingPassGeneratedIdReturn;
        public Object airlineScheduleStatus;
        public String departureCity;
        public int returnTimezone;
        public boolean boardingPassReturn;
        public boolean enableOnlineCheckinReturn;
        public String arrivalCity;
        public String returnTime;
        public int returnOrderDetailId;
        public double totalAmount;
        public boolean boardingPassDepart;
        public int adultTotal;
        public boolean smartRoundTrip;
        public Object urlOnlineCheckinListDepart;
        public String customerCurrency;
    }
}