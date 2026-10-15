package com.tiket.pojo.flight;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class FlightBookingDetail extends CompletableFuture<FlightBookingDetail> {
    public Data data;
    public OrderResponse orderResponse;

    public static class AddonsResult{
        public ArrayList<Paxis> paxes;
        public TotalAddonsFare totalAddonsFare;
        public Baggages baggages;
        public Meals meals;
        public Seats seats;
    }

    public static class Baggage{
        public int departureBaggage;
        public int returnBaggage;
        public ArrayList<DepartureBaggage> departureBaggages;
        public ArrayList<ReturnBaggage> returnBaggages;
        public String departure;
        public String arrival;
        public int unit;
        public String measurement;
        public boolean success;
    }

    public static class Baggages{
        public boolean departureResult;
        public boolean returnResult;
    }

    public static class BaggageSuccessAddonsResult{
        public boolean departureResult;
        public boolean returnResult;
    }

    public static class CartTotalFare{
        public int total;
        public int totalWithoutAdjustment;
        public int totalWithMarkupWithoutDiscount;
        public int base;
        public int commission;
        public int iwjr;
        public int tax;
        public int markup;
        public int discount;
        public int seatAvailables;
        public int supplierDiscount;
        public int loyaltyPoint;
        public int insurance;
        public Object premiNumber;
        public int totalWithInsurance;
        public Object penaltyInfos;
        public Object fareBasisCodes;
        public Object refundInformation;
        public Object adjustmentId;
        public Object campaignId;
        public int d5;
    }

    public static class Contact{
        public String profileId;
        public String firstName;
        public String lastName;
        public Object dob;
        public String phoneCode;
        public String phoneNumber;
        public String title;
        public String gender;
        public String email;
        public String emailPassenger;
        public String emailAgent;
        public String accountId;
    }

    public static class Data{

        public Object contactErrors;
        public Object adultErrors;
        public Object childErrors;
        public Object infantErrors;
        public OrderResponse orderResponse;
        public boolean doubleBooking;
        public MultiCurrency multiCurrency;
    }

    public static class DepartureAddonsResultResponse{
        public ArrayList<Baggage> baggage;
        public ArrayList<Object> meals;
        public ArrayList<Object> seat;
    }

    public static class DepartureBaggage{
        public String unitOfMeasure;
        public int weight;
    }

    public static class DepartureDetail{
        public String journeyType;
        public String origin;
        public String destination;
        public String airlineCode;
        public String flightSelect;
        public ArrayList<Object> codeDetailChanges;
    }

    public static class DepartureFareClass{
        public String origin;
        public String destination;
        public String oldInclusiveBaggageMeasurement;
        public String oldInclusiveBaggage;
        public String newInclusiveBaggageMeasurement;
        public String newInclusiveBaggage;
        public boolean equalFareClass;
    }

    public static class DepartureInclusiveBaggage{
        public String origin;
        public String destination;
        public String oldInclusiveBaggageMeasurement;
        public String oldInclusiveBaggage;
        public String newInclusiveBaggageMeasurement;
        public String newInclusiveBaggage;
    }

    public static class Fare{
        public int total;
        public int totalWithoutAdjustment;
        public int totalWithMarkupWithoutDiscount;
        public int base;
        public int commission;
        public int iwjr;
        public int tax;
        public int markup;
        public int discount;
        public int seatAvailables;
        public int supplierDiscount;
        public int loyaltyPoint;
        public int insurance;
        public Object premiNumber;
        public int totalWithInsurance;
        public Object adjustmentId;
        public Object campaignId;
        public int d5;
    }

    public static class FareClassCompareResult{
        public ArrayList<DepartureFareClass> departureFareClass;
        public Object returnFareClass;
        public boolean equalFareClass;
    }

    public static class InclusiveBaggageSchedules{
        public ArrayList<DepartureInclusiveBaggage> departureInclusiveBaggage;
        public Object returnInclusiveBaggage;
    }

    public static class Meals{
        public ArrayList<Object> departureMeals;
        public ArrayList<Object> returnMeals;
        public boolean departureResult;
        public boolean returnResult;
    }

    public static class MealsSuccessAddonsResult{
        public boolean departureResult;
        public boolean returnResult;
    }

    public static class MultiCurrency{
        public String currency;
        public int scale;
    }

    public static class OrderDetailChanges{
        public String flightType;
        public String itineraryType;
        public ArrayList<Object> codeDetailChanges;
        public PriceChangesReason priceChangesReason;
        public DepartureDetail departureDetail;
        public Object returnDetail;
    }

    public static class OrderResponse{
        public Contact contact;
        public String status;
        public String origin;
        public String destination;
        public String departureDate;
        public Object returnDate;
        public String orderId;
        public String orderHash;
        public String paymentUrl;
        public String bookingCurrency;
        public String cartCurrency;
        public String bookingExpired;
        public String paymentExpired;
        public ArrayList<Object> bookingInformations;
        public int totalFare;
        public int totalFareWithInsuranceWithBaggage;
        public int totalFareWithoutInsuranceWithBaggage;
        public int totalFareWithoutInsuranceWithoutBaggage;
        public int totalOldFare;
        public int totalInsurance;
        public int totalMultiInsurance;
        public int totalBaggageDeparture;
        public int totalBaggageReturn;
        public int totalMealDeparture;
        public int totalMealReturn;
        public int totalSeatDeparture;
        public int totalSeatReturn;
        public int totalBundlingAddonsDeparture;
        public int totalBundlingAddonsReturn;
        public ArrayList<CartTotalFare> cartTotalFares;
        public ArrayList<Fare> fares;
        public Object oldFares;
        public FareClassCompareResult fareClassCompareResult;
        public UpdatedBookInfo updatedBookInfo;
        public boolean equalPenaltyInfoFare;
        public boolean equalFare;
        public boolean doubleBooking;
    }

    public static class Passport{
        public String id;
        public String expired;
        public String country;
        public String issuingDate;
    }

    public static class Paxis{
        public int paxNumber;
        public String profileId;
        public Object type;
        public String title;
        public String gender;
        public String firstName;
        public String lastName;
        public String nationality;
        public String dob;
        public Object phoneCode;
        public Object phoneNumber;
        public Object identityNumber;
        public Object travelPurpose;
        public Passport passport;
        public Baggage baggage;
        public Meals meals;
        public Seats seats;
        public Object frequentFlyer;
        public boolean insurance;
        public Object originalFirstName;
        public Object originalLastName;
        public Object bundlingAddonsPassenger;
        public BaggageSuccessAddonsResult baggageSuccessAddonsResult;
        public MealsSuccessAddonsResult mealsSuccessAddonsResult;
        public SeatSuccessAddonsResult seatSuccessAddonsResult;
        public DepartureAddonsResultResponse departureAddonsResultResponse;
        public ReturnAddonsResultResponse returnAddonsResultResponse;
    }

    public static class PriceChangesReason{
        public Object defaultMessage;
        public ArrayList<Object> reasons;
    }

    public static class ReturnAddonsResultResponse{
        public ArrayList<Baggage> baggage;
        public ArrayList<Object> meals;
        public ArrayList<Object> seat;
    }

    public static class ReturnBaggage{
        public String unitOfMeasure;
        public int weight;
    }

    public static class Root{
        public String code;
        public String message;
        public Object errors;
        public Data data;
        public long serverTime;
    }

    public static class Seats{
        public ArrayList<Object> departureSeats;
        public ArrayList<Object> returnSeats;
        public boolean departureResult;
        public boolean returnResult;
    }

    public static class SeatSuccessAddonsResult{
        public boolean departureResult;
        public boolean returnResult;
    }

    public static class TotalAddonsFare{
        public int baggage;
        public int meals;
        public int seat;
    }

    public static class UpdatedBookInfo{
        public boolean equalFare;
        public boolean doubleBooking;
        public int totalFare;
        public int totalOldFare;
        public ArrayList<Fare> fares;
        public Object oldFares;
        public InclusiveBaggageSchedules inclusiveBaggageSchedules;
        public AddonsResult addonsResult;
        public OrderDetailChanges orderDetailChanges;
    }
}
