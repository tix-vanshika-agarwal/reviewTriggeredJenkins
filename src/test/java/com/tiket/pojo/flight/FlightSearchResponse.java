package com.tiket.pojo.flight;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class FlightSearchResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data{
        public boolean searchCompleted;
        public ArrayList<String> requestItems;
        public EventTermAndConditionResponses eventTermAndConditionResponses;
        public Object eventTermAndConditionRule;
        public Object refundProperties;
        public Object rescheduleProperties;
        public CampaignCountdown campaignCountdown;
        public HashMap<String,AirlineDetail> airlines;
        public HashMap<String,AirportDetail> airports;
        public Facilities facilities;
        public SearchList searchList;
        public MultiCurrency multiCurrency;
    }

    public static class Airline{
        ArrayList<HashMap<String,AirlineDetail>> airline;
    }

    public static class AirlineDetail{
        public String code;
        public String name;
        public String displayName;
        public String shortName;
        public String urlIcon;
    }

    public static class EventTermAndConditionResponses{
        public Reschedule reschedule;
        public Refund refund;
    }

    public static class Reschedule{
        public String title;
        public String subtitle;
        public Object eventTermAndCondition;
        public String desc;
        public int amountAdult;
        public Object amountChild;
        public Object amountInfant;
    }

    public static class Refund{
        public String title;
        public String subtitle;
        public Object eventTermAndCondition;
        public String desc;
        public int amountAdult;
        public Object amountChild;
        public Object amountInfant;
    }

    public static class CampaignCountdown{
        public BigInteger remainingTimeInMillis;
        public String endDateTime;
        public ArrayList<Object> campaignLabels;
    }

    public static class Facilities{
        public Meal meal;
        public AverageDepartureDelayTime averageDepartureDelayTime;
        public Wifi wifi;
        public Entertainment entertainment;
        public Usb usb;
        public Punctuality punctuality;
        public Aircraft aircraft;
        public SeatLayout seatLayout;
    }

    public static class Meal{
        public String icon;
    }

    public static class AverageDepartureDelayTime{
        public Object icon;
    }

    public static class Wifi{
        public String icon;
    }

    public static class Entertainment{
        public String icon;
    }

    public static class Punctuality{
        public Object icon;
    }

    public static class Aircraft{
        public Object icon;
    }

    public static class Usb{
        public String icon;
    }

    public static class SeatLayout{
        public Object icon;
    }

    public static class SearchList{
        public ArrayList<Flight> departureFlights;
        public ArrayList<Flight> returnFlights;
        public ArrayList<Object> srtReturnFlights;
        public ArrayList<Object> roundTrips;
        public ArrayList<Object> rtsRoundTrips;
    }

    public static class Flight{
        public String flightId;
        public String flightSelect;
        public String currency;
        public String departureAirportCode;
        public String arrivalAirportCode;
        public String departureTime;
        public String arrivalTime;
        public int totalTravelTimeInMinutes;
        public int totalTransitTimeInMinutes;
        public int totalTransit;
        public int seatAvailability;
        public boolean international;
        public ArrayList<String> facilitiesPriority;
        public boolean refundable;
        public String journeySellKey;
        public boolean roundTrip;
        public String cabinClass;
        public boolean withInsurance;
        public ArrayList<Refund> refund;
        public ArrayList<Reschedule> reschedule;
        public PenaltyFeeDescriptions penaltyFeeDescriptions;
        public ArrayList<String> tags;
        public ArrayList<Object> groupTags;
        public ArrayList<String> facilityTags;
        public LabelTags labelTags;
        public String marketingAirlineCode;
        public FareDetail fareDetail;
        public ArrayList<Schedule> schedules;
        public String fareId;
        public String noShowFeeRefund;
        public String noShowFeeReschedule;
        public String fareSellKey;
    }

    public static class PenaltyFeeDescriptions{
        public String reschedule;
        public String refund;
    }

    public static class LabelTags{
        public ArrayList<String> policyInfo;
        public ArrayList<String> benefitInfo;
    }


    public static class FareDetail{
        public Object fareBasisCode;
        public String fareClass;
        public int cheapestFare;
        public Object marketingAirline;
        public ArrayList<Fare> fares;
    }

    public static class Fare{
        public String supplierId;
        public String distributionType;
        public String currency;
        public int cheapestFare;
        public TotalFare totalFare;
        public AdultFare adultFare;
        public ChildFare childFare;
        public InfantFare infantFare;
        public ArrayList<Object> campaignLabels;
    }

    public static class TotalFare{
        public int total;
        public int totalWithoutAdjustment;
        public int totalWithMarkupWithoutDiscount;
        public int totalWithInsurance;
        public int totalWithoutAdjustmentWithInsurance;
        public int totalWithMarkupWithoutDiscountWithInsurance;
        public Object discountDetail;
        public int loyaltyPoint;
        public int seatAvailables;
        public int insurance;
    }

    public static class AdultFare{
        public int total;
        public int totalWithoutAdjustment;
        public int totalWithMarkupWithoutDiscount;
        public int totalWithInsurance;
        public int totalWithoutAdjustmentWithInsurance;
        public int totalWithMarkupWithoutDiscountWithInsurance;
        public Object discountDetail;
        public int loyaltyPoint;
        public int seatAvailables;
        public int insurance;
    }

    public static class ChildFare{
        public int total;
        public int totalWithoutAdjustment;
        public int totalWithMarkupWithoutDiscount;
        public int totalWithInsurance;
        public int totalWithoutAdjustmentWithInsurance;
        public int totalWithMarkupWithoutDiscountWithInsurance;
        public Object discountDetail;
        public int loyaltyPoint;
        public int seatAvailables;
        public int insurance;
    }

    public static class InfantFare{
        public int total;
        public int totalWithoutAdjustment;
        public int totalWithMarkupWithoutDiscount;
        public int totalWithInsurance;
        public int totalWithoutAdjustmentWithInsurance;
        public int totalWithMarkupWithoutDiscountWithInsurance;
        public Object discountDetail;
        public int loyaltyPoint;
        public int seatAvailables;
        public int insurance;
    }

    public static class Schedule{
        public String fareClass;
        public String flightNumber;
        public String cabinClass;
        public DepartureDetail departureDetail;
        public ArrivalDetail arrivalDetail;
        public int totalTravelTimeInMinutes;
        public int totalTransitTimeInMinutes;
        public TravelTime travelTime;
        public TransitTime transitTime;
        public Baggage baggage;
        public ArrayList<Object> connectings;
        public boolean facilitiesReady;
        public FacilitiesValue facilitiesValue;
        public String operatingFlightNumber;
        public boolean bundlingMeal;
        public boolean bundlingBaggage;
        public ArrayList<Object> tags;
        public ArrayList<String> facilityTags;
        public String airlineCode;
        public String operatingAirlineCode;
    }

    public static class DepartureDetail{
        public String date;
        public String time;
        public double timezone;
        public String airportCode;
        public String terminal;
        public boolean visaRequired;
    }

    public static class ArrivalDetail{
        public String date;
        public String time;
        public double timezone;
        public String airportCode;
        public String terminal;
        public boolean visaRequired;
    }

    public static class TransitTime{
        public int day;
        public int hour;
        public int minute;
    }

    public static class TravelTime{
        public int day;
        public int hour;
        public int minute;
    }

    public static class Baggage{
        public Cabin cabin;
        public CheckIn checkIn;
        public boolean additionalBaggage;
        public String urlIcon;
    }

    public static class Cabin{
        public int unit;
        public String measurement;
        public int qty;
        public String desc;
    }

    public static class CheckIn{
        public int unit;
        public String measurement;
        public int qty;
        public String desc;
    }

    public static class FacilitiesValue{
        public String meal;
        public Object averageDepartureDelayTime;
        public String wifi;
        public String usb;
        public String entertainment;
        public Object punctuality;
        public String aircraft;
        public Object seatLayout;
    }

    public static class MultiCurrency{
        public int scale;
        public String currency;
    }

    public static class AirportDetail {
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String countryCode;
    }
}
