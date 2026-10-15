package com.tiket.pojo.b2b.corporate;

public class PostInterimTransactionsApiBodyFlight {
    public String productType;
    public String verticalCartID;
    public BookingCriteria bookingCriteria;
    public String travelPlanDetailID;
    public java.util.List<String> customFields;
    public java.util.List<String> costCenters;

    public static class BookingCriteria{
        public Flight flight;
    }

    public static class Flight{
        public String productType;
        public String itineraryType;
        public String depart;
        @com.google.gson.annotations.SerializedName("return")
        public String returnValue;
        public String segment;
        public String cartId;
        public String departureAirportCode;
        public String departureCityCode;
        public String arrivalAirportCode;
        public String arrivalCityCode;
        public String isRoundTrip;
        public String departureDate;
        public String returnDate;
    }
}
