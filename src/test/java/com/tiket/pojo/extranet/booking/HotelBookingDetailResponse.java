package com.tiket.pojo.extranet.booking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotelBookingDetailResponse {
    @SerializedName("code")
    public String code;
    
    @SerializedName("message")
    public String message;
    
    @SerializedName("errors")
    public Object errors;
    
    @SerializedName("data")
    public Data data;
    
    @SerializedName("serverTime")
    public Long serverTime;
    
    public static class Data {
        @SerializedName("guestName")
        public String guestName;
        
        @SerializedName("itineraryId")
        public Integer itineraryId;
        
        @SerializedName("status")
        public String status;
        
        @SerializedName("totalAdult")
        public Integer totalAdult;
        
        @SerializedName("totalChild")
        public Integer totalChild;
        
        @SerializedName("totalRoom")
        public Integer totalRoom;
        
        @SerializedName("checkIn")
        public String checkIn;
        
        @SerializedName("checkOut")
        public String checkOut;
        
        @SerializedName("totalNight")
        public Integer totalNight;
        
        @SerializedName("roomType")
        public String roomType;
        
        @SerializedName("ratePlan")
        public String ratePlan;
        
        @SerializedName("promos")
        public List<String> promos;
        
        @SerializedName("valueAdded")
        public Object valueAdded;
        
        @SerializedName("specialRequest")
        public String specialRequest;
        
        @SerializedName("cancellationPolicy")
        public String cancellationPolicy;
        
        @SerializedName("nettAmount")
        public Double nettAmount;
        
        @SerializedName("currency")
        public String currency;
        
        @SerializedName("paymentMethod")
        public String paymentMethod;
        
        @SerializedName("paymentStatus")
        public String paymentStatus;
        
        @SerializedName("refundType")
        public Object refundType;
        
        @SerializedName("refundAmount")
        public Object refundAmount;
        
        @SerializedName("homesPolicyUrl")
        public Object homesPolicyUrl;
        
        @SerializedName("homesInsurancePackage")
        public Object homesInsurancePackage;
        
        @SerializedName("roomId")
        public Integer roomId;
        
        @SerializedName("ratePlanId")
        public Integer ratePlanId;
        
        @SerializedName("roomCode")
        public Object roomCode;
        
        @SerializedName("ratePlanCode")
        public Object ratePlanCode;
        
        @SerializedName("bookingTime")
        public String bookingTime;
    }
    
    /**
     * Checks if the API call was successful
     * @return true if response code is SUCCESS, false otherwise
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(code);
    }
    
    /**
     * Checks if the booking status is issued
     * @return true if booking status is "issued", false otherwise
     */
    public boolean isIssued() {
        return data != null && "issued".equalsIgnoreCase(data.status);
    }
    
    /**
     * Checks if payment is pending
     * @return true if payment status is "PENDING", false otherwise
     */
    public boolean isPaymentPending() {
        return data != null && "PENDING".equalsIgnoreCase(data.paymentStatus);
    }
    
    /**
     * Gets the booking/itinerary ID
     * @return the itinerary ID, or null if data is null
     */
    public Integer getItineraryId() {
        return data != null ? data.itineraryId : null;
    }
    
    /**
     * Gets the booking status
     * @return the booking status, or null if data is null
     */
    public String getStatus() {
        return data != null ? data.status : null;
    }
}

