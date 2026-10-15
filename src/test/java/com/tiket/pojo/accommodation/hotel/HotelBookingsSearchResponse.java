package com.tiket.pojo.accommodation.hotel;

import java.util.List;
import java.util.Map;

public class HotelBookingsSearchResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public List<BookingContent> content;
        public Pageable pageable;
        public boolean last;
        public int totalElements;
        public int totalPages;
        public boolean first;
        public int size;
        public int number;
        public Sort sort;
        public int numberOfElements;
        public boolean empty;
    }

    public static class BookingContent {
        public long id;
        public String roomCode;
        public String name;
        public String bookingTime;
        public long checkinTime;
        public long checkoutTime;
        public String checkinDate;
        public String checkoutDate;
        public int duration;
        public String ratePlanName;
        public double netPrice;
        public double addOnsNetPrice;
        public double totalNetPrice;
        public String netCurrency;
        public String promotion;
        public String status;
        public String specialRequest;
        public Object cancellationPolicy;
        public String currency;
        public String roomName;
        public Map<String, Double> ratePerNight;
        public int totalRoom;
        public int adult;
        public double commissionPrice;
        public AddOnDetails[] addOnDetails;
        public boolean showDetails;
        public int vccStatus;
        public Object benefit;
        public String membershipLevel;
        public String pricingModel;
        public String storeId;
        public Object refundType;
        public double refundAmount;
        public Object homesTotalPremium;
        public Object homesInsurancePackage;
        public Object homesPolicyUrl;
        public List<String> guests;
        public int orderId;
    }

    public static class AddOnDetails {
        public String type;
        public String date;
        public int roomIndex;
        public String categoryName;
        public String name;
        public int amount;
        public double netPrice;
    }

    public static class Pageable {
        public int pageNumber;
        public int pageSize;
        public Sort sort;
        public int offset;
        public boolean paged;
        public boolean unpaged;
    }

    public static class Sort {
        public boolean empty;
        public boolean sorted;
        public boolean unsorted;
    }
}

