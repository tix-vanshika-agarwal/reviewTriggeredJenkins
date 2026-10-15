package com.tiket.pojo.flight;

import java.util.ArrayList;

public class FlightBookingDetailDomestic {
    public Data data;
    public long serverTime;
    public String message;
    public Object errors;
    public OrderResponse orderResponse;

    public static class AddonsResult{
        public TotalAddonsFare totalAddonsFare;
        public Baggages baggages;
        public ArrayList<Paxis> paxes;
        public Seats seats;
        public Meals meals;
    }

    public static class Baggage{
        public int unit;
        public String arrival;
        public boolean success;
        public String departure;
        public String measurement;
        public ArrayList<DepartureBaggage> departureBaggages;
        public int returnBaggage;
        public int departureBaggage;
        public ArrayList<ReturnBaggage> returnBaggages;
    }

    public static class Baggages{
        public boolean returnResult;
        public boolean departureResult;
    }

    public static class BaggageSuccessAddonsResult{
        public boolean returnResult;
        public boolean departureResult;
    }

    public static class CartTotalFare{
        public int insurance;
        public int markup;
        public Object premiNumber;
        public int totalWithInsurance;
        public Object campaignId;
        public int iwjr;
        public int discount;
        public int tax;
        public int seatAvailables;
        public int totalWithoutAdjustment;
        public int loyaltyPoint;
        public Object penaltyInfos;
        public int d5;
        public int total;
        public int supplierDiscount;
        public Object fareBasisCodes;
        public Object refundInformation;
        public int totalWithMarkupWithoutDiscount;
        public int commission;
        public Object adjustmentId;
        public int base;
    }

    public static class Contact{
        public String firstName;
        public String lastName;
        public String accountId;
        public String phoneNumber;
        public String gender;
        public String emailAgent;
        public String profileId;
        public Object dob;
        public String phoneCode;
        public String emailPassenger;
        public String title;
        public String email;
    }

    public static class Data{
        public Object infantErrors;
        public Object contactErrors;
        public Object childErrors;
        public boolean doubleBooking;
        public OrderResponse orderResponse;
        public Object multiCurrency;
        public Object adultErrors;
        public SuccessData successData;
    }

    public static class SuccessData {
        public long orderId;
        public String orderHash;
        public String paymentUrl;
    }

    public static class DepartureAddonsResultResponse{
        public ArrayList<Object> seat;
        public ArrayList<Baggage> baggage;
        public ArrayList<Object> meals;
    }

    public static class DepartureBaggage{
        public String unitOfMeasure;
        public int weight;
    }

    public static class DepartureDetail{
        public String flightSelect;
        public String journeyType;
        public ArrayList<Object> codeDetailChanges;
        public String origin;
        public String destination;
        public String airlineCode;
    }

    public static class DepartureFareClass{
        public String newInclusiveBaggageMeasurement;
        public String oldInclusiveBaggageMeasurement;
        public boolean equalFareClass;
        public String newInclusiveBaggage;
        public String oldInclusiveBaggage;
        public String origin;
        public String destination;
    }

    public static class DepartureInclusiveBaggage{
        public String newInclusiveBaggageMeasurement;
        public String oldInclusiveBaggageMeasurement;
        public String newInclusiveBaggage;
        public String oldInclusiveBaggage;
        public String origin;
        public String destination;
    }

    public static class Fare{
        public int insurance;
        public int markup;
        public Object premiNumber;
        public int totalWithInsurance;
        public Object campaignId;
        public int iwjr;
        public int discount;
        public int tax;
        public int seatAvailables;
        public int totalWithoutAdjustment;
        public int loyaltyPoint;
        public int d5;
        public int total;
        public int supplierDiscount;
        public int totalWithMarkupWithoutDiscount;
        public int commission;
        public Object adjustmentId;
        public int base;
    }

    public static class FareClassCompareResult{
        public ArrayList<DepartureFareClass> departureFareClass;
        public boolean equalFareClass;
        public Object returnFareClass;
    }

    public static class InclusiveBaggageSchedules{
        public Object returnInclusiveBaggage;
        public ArrayList<DepartureInclusiveBaggage> departureInclusiveBaggage;
    }

    public static class Meals{
        public ArrayList<Object> returnMeals;
        public ArrayList<Object> departureMeals;
        public boolean returnResult;
        public boolean departureResult;
    }

    public static class MealsSuccessAddonsResult{
        public boolean returnResult;
        public boolean departureResult;
    }

    public static class OrderDetailChanges{
        public String itineraryType;
        public ArrayList<Object> codeDetailChanges;
        public PriceChangesReason priceChangesReason;
        public Object returnDetail;
        public String flightType;
        public DepartureDetail departureDetail;
    }

    public static class OrderResponse{
        public int totalFare;
        public int totalFareWithoutInsuranceWithoutBaggage;
        public int totalBundlingAddonsDeparture;
        public String orderId;
        public String origin;
        public int totalOldFare;
        public String destination;
        public int totalInsurance;
        public String bookingCurrency;
        public int totalMealReturn;
        public boolean equalPenaltyInfoFare;
        public ArrayList<Fare> fares;
        public int totalSeatReturn;
        public Object returnDate;
        public Contact contact;
        public int totalBaggageDeparture;
        public String departureDate;
        public int totalSeatDeparture;
        public String orderHash;
        public ArrayList<Object> bookingInformations;
        public int totalMultiInsurance;
        public int totalBundlingAddonsReturn;
        public int totalFareWithInsuranceWithBaggage;
        public String paymentExpired;
        public ArrayList<CartTotalFare> cartTotalFares;
        public String cartCurrency;
        public UpdatedBookInfo updatedBookInfo;
        public int totalBaggageReturn;
        public boolean doubleBooking;
        public String paymentUrl;
        public Object oldFares;
        public String bookingExpired;
        public boolean equalFare;
        public int totalFareWithoutInsuranceWithBaggage;
        public FareClassCompareResult fareClassCompareResult;
        public String status;
        public int totalMealDeparture;
    }

    public static class Passport{
        public Object country;
        public Object expired;
        public Object issuingDate;
        public Object id;
    }

    public static class Paxis{
        public boolean insurance;
        public ReturnAddonsResultResponse returnAddonsResultResponse;
        public String lastName;
        public MealsSuccessAddonsResult mealsSuccessAddonsResult;
        public String gender;
        public Baggage baggage;
        public Object bundlingAddonsPassenger;
        public Object type;
        public String title;
        public Seats seats;
        public DepartureAddonsResultResponse departureAddonsResultResponse;
        public Passport passport;
        public Object identityNumber;
        public BaggageSuccessAddonsResult baggageSuccessAddonsResult;
        public Object phoneCode;
        public SeatSuccessAddonsResult seatSuccessAddonsResult;
        public Object originalLastName;
        public Object originalFirstName;
        public Object travelPurpose;
        public String firstName;
        public Object frequentFlyer;
        public Object phoneNumber;
        public String nationality;
        public String profileId;
        public String dob;
        public int paxNumber;
        public Meals meals;
    }

    public static class PriceChangesReason{
        public ArrayList<Object> reasons;
        public Object defaultMessage;
    }

    public static class ReturnAddonsResultResponse{
        public ArrayList<Object> seat;
        public ArrayList<Baggage> baggage;
        public ArrayList<Object> meals;
    }

    public static class ReturnBaggage{
        public String unitOfMeasure;
        public int weight;
    }

    public static class Seats{
        public ArrayList<Object> returnSeats;
        public ArrayList<Object> departureSeats;
        public boolean returnResult;
        public boolean departureResult;
    }

    public static class SeatSuccessAddonsResult{
        public boolean returnResult;
        public boolean departureResult;
    }

    public static class TotalAddonsFare{
        public int seat;
        public int baggage;
        public int meals;
    }

    public static class UpdatedBookInfo{
        public int totalFare;
        public AddonsResult addonsResult;
        public boolean equalFare;
        public OrderDetailChanges orderDetailChanges;
        public int totalOldFare;
        public boolean doubleBooking;
        public InclusiveBaggageSchedules inclusiveBaggageSchedules;
        public ArrayList<Fare> fares;
        public Object oldFares;
    }
}
