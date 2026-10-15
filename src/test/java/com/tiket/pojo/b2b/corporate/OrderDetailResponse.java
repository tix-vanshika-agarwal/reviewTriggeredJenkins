package com.tiket.pojo.b2b.corporate;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailResponse {
    public boolean success;
    public ArrayList<Data> data;
    public Metadata metadata;

    public static class Data{
        public String approvalEmail;
        public String approvalNotes;
        public String employeeDivision;
        public String employeeEmail;
        public String employeeName;
        /**
         * orderDetail is polymorphic: for flight orders the API returns an array of entries
         * (each with {@code depart}/{@code return}), but for hotel/single-leg orders it returns a single object.
         * Normalize both to a List via the custom deserializer below.
         */
        @JsonAdapter(OrderDetailListAdapter.class)
        public List<OrderDetail> orderDetail;
        public int orderID;
        public String orderHash;
        public int referenceOrderID;
        public String referenceTable;
        public Date paymentExpiredDate;
        public String paymentStatus;
        public String orderStatus;
        public String productType;
        public String totalPrice;
        public ArrayList<Traveler> travelers;
        public String orderNotes;
        public Date purchaseDate;
        public ArrayList<Object> refundDetail;
        public ArrayList<Object> rescheduleDetail;
        public boolean approvable;
        public TravelPolicyDetail travelPolicyDetail;
        public EmployeeGroups employeeGroups;
    }

    public static class TravelPolicyDetail{
        public int orderID;
        public boolean complyTravelPolicy;
        public Policy policy;
    }

    public static class Policy{
        public int id;
        public int travelPolicyID;
        public String productType;
        public Config config;
    }

    public static class Config{
        public boolean addOns;
        public ArrayList<Detail> details;
        public boolean insurance;
        public String policyType;
        public int advanceBookingDay;
    }

    public static class Detail{
        public String currency;
        public int maxPrice;
        public int minPrice;
        public ArrayList<Object> countries;
        public ArrayList<String> starRatings;
        public String categoryName;
    }

    public static class Traveler{
        public String salutation;
        public String fullName;
        public Vertical vertical;
    }

    public static class Vertical{
        public String busSeat;
    }

    public static class EmployeeGroups{
        public int divisionID;
        public int groupID;
        public String divisionName;
        public String groupName;
    }

    public static class Metadata{
        public int page;
        public int size;
        public int count;
        public int totalCount;
        public ExtInfo extInfo;
    }

    public static class OrderDetail{
        public Depart depart;

        @SerializedName("return")
        public Return myreturn;
        public int orderDetailId;
        public String hotelName;
        public int guest;
        public String checkinDate;
        public String checkoutDate;
        public int quantity;
        public int duration;
        public HotelDetail hotelDetail;
    }

    public static class Depart{
        public String airlinesName;
        public String corporateCode;
        public String corporateCodeType;
    }

    public static class Return{
        public String airlinesName;
        public String corporateCode;
        public String corporateCodeType;
    }


    public static class ExtInfo{
    }

    public static class HotelDetail{
        public String typeRoom;
        public int roomNumber;
        public String status;
        public Object specialRequest;
        public int starRating;
    }
}
