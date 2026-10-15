package com.tiket.pojo.flight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlightStreamResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime; // Changed BigInteger to long for consistency and simplicity

    public static class Data{
        public boolean searchCompleted;
        public Object allRequestItems;
        public List<String> requestItems; // Use List instead of ArrayList
        public SearchList searchList;
        public Map<String, AirlineDetail> airlines; // Use Map instead of HashMap
        public Map<String, AirportDetail> airports; // Use Map instead of HashMap
        public Object specialFareAirlines;
        public Object generalIcons;
        public List<Object> discountLabelProperties; // Use List instead of ArrayList
        public List<Object> additionalLabelProperties; // Use List instead of ArrayList
        public List<Object> tagProperties; // Use List instead of ArrayList
        public Object eventTermAndConditionResponses;
        public Object eventTermAndConditionRule;
        public Object refundProperties;
        public Object rescheduleProperties;
        public Object campaignCountdown;
        public Long departureFlightDeltaPrice;
    }

    public static class AirportDetail {
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String countryCode;
    }

    public static class AirlineDetail{
        public String code;
        public String name;
        public String displayName;
        public String shortName;
        public String urlIcon;
    }

    public static class SearchList{
        public List<DepartureFlight> departureFlights;
        public List<ReturnFlight> returnFlights;
        public List<ReturnFlight> srtReturnFlights;
        public List<Object> roundTrips;
        public List<Object> rtsRoundTrips;
    }

    public static class DepartureFlight{
        public String flightId;
        public String flightIdGeneral;
        public String lyraKey;
        public String flightIdLyra;
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
        public boolean refundable;
        public String journeySellKey;
        public String fareSellKey;
        public boolean roundTrip;
        public String cabinClass;
        public boolean withInsurance;
        public String marketingAirlineCode;
        public FareDetail fareDetail;
        public String fareId;
        public String noShowFeeRefund;
        public String noShowFeeReschedule;
        public boolean virtualInterlining;
        public Object corporatePolicy;
        public List<String> facilitiesPriority;
        public List<Refund> refund;
        public List<Reschedule> reschedule;
        public List<Object> schedulePolicies;
        public List<String> tags;
        public List<String> groupTags;
        public List<String> facilityTags;
        public List<Schedule> schedules;
        public PenaltyFeeDescriptions penaltyFeeDescriptions;
        public LabelTags labelTags;
        public String airlineName;
    }

    public class LabelTags{
        public List<String> policyInfo;
        public List<String> benefitInfo;
        public Object otherInfo;
    }

    public static class Departure{
        public String date;
        public String time;
        public double timezone;
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String terminal;
        public String countryCode;
        public boolean visaRequired;
        public double timeZoneDaylightSaving;
    }

    public static class Arrival{
        public String date;
        public String time;
        public double timezone;
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String terminal;
        public String countryCode;
        public boolean visaRequired;
        public double timeZoneDaylightSaving;
    }

    public static class Refund{
        public String desc;
        public double amountAdult;
        public Object amountChild;
        public Object amountInfant;
        public String title;
        public String subtitle;
        public Object eventTermAndCondition;
    }

    public static class Reschedule{
        public String desc;
        public double amountAdult;
        public Object amountChild;
        public Object amountInfant;
        public String title;
        public String subtitle;
        public Object eventTermAndCondition;
    }

    public static class PenaltyFeeDescriptions{
        public String reschedule;
        public String refund;
    }

    public static class marketingAirlineCode{
        public String code;
        public String name;
        public String displayName;
        public String shortName;
        public Object url;
        public String urlIcon;
    }

    public static class FareDetail{
        public Object fareBasisCode;
        public String fareClass;
        public double cheapestFare;
        public Object marketingAirline;
        public List<Fare> fares;
    }

    public static class Fare{
        public String supplierId;
        public String distributionType;
        public String currency;
        public double cheapestFare;
        public TotalFare totalFare;
        public AdultFare adultFare;
        public ChildFare childFare;
        public InfantFare infantFare;
        public PromoData promoData;
        public List<String> discountLabels;
        public List<String> campaignLabels;
    }

    public static class PromoData {
        public String promoCode;
        public double amount;
        public double prorateAmount;
        public double cashback;
        public double prorateCashback;
        public double totalPrice;
        public double adultPrice;
        public double childPrice;
        public double infantPrice;
        public double totalDiscount;
        public double totalAllDisc;
        public double prorateAllDisc;
        public List<String> paymentMethodList;
        public Object paymentMethodString;
        public boolean needUserLogin;
        public long expiredDate;
        public String promoType;
        public double payNow;
        public double totalAll;
    }

    public static class TotalFare{
        public double total;
        public double totalWithoutAdjustment;
        public double totalWithMarkupWithoutDiscount;
        public double totalWithInsurance;
        public double totalWithoutAdjustmentWithInsurance;
        public double totalWithMarkupWithoutDiscountWithInsurance;
        public double base;
        public double iwjr;
        public double tax;
        public double discount;
        public DiscountDetail discountDetail;
        public double loyaltyPoint;
        public int seatAvailables;
        public double insurance;
        public double convenienceFee;
    }

    public static class AdultFare{
        public double total;
        public double totalWithoutAdjustment;
        public double totalWithMarkupWithoutDiscount;
        public double totalWithInsurance;
        public double totalWithoutAdjustmentWithInsurance;
        public double totalWithMarkupWithoutDiscountWithInsurance;
        public double base;
        public double iwjr;
        public double tax;
        public double discount;
        public DiscountDetail discountDetail;
        public double loyaltyPoint;
        public int seatAvailables;
        public double insurance;
        public double convenienceFee;
    }

    public static class ChildFare{
        public double total;
        public double totalWithoutAdjustment;
        public double totalWithMarkupWithoutDiscount;
        public double totalWithInsurance;
        public double totalWithoutAdjustmentWithInsurance;
        public double totalWithMarkupWithoutDiscountWithInsurance;
        public double base;
        public double iwjr;
        public double tax;
        public double discount;
        public Object discountDetail;
        public double loyaltyPoint;
        public int seatAvailables;
        public double insurance;
        public double convenienceFee;
    }

    public static class InfantFare{
        public double total;
        public double totalWithoutAdjustment;
        public double totalWithMarkupWithoutDiscount;
        public double totalWithInsurance;
        public double totalWithoutAdjustmentWithInsurance;
        public double totalWithMarkupWithoutDiscountWithInsurance;
        public double base;
        public double iwjr;
        public double tax;
        public double discount;
        public Object discountDetail;
        public double loyaltyPoint;
        public int seatAvailables;
        public double insurance;
        public double convenienceFee;
    }

    public static class DiscountDetail{
        public double total;
        public List<Breakdown> breakdowns;
    }

    public static class Breakdown{
        public String code;
        public double value;
        public boolean displayLabel;
    }

    public static class Schedule{
        public String fareClass;
        public String flightNumber;
        public Aircraft aircraft;
        public String cabinClass;
        public DepartureDetail departureDetail;
        public ArrivalDetail arrivalDetail;
        public int totalTravelTimeInMinutes;
        public int totalTransitTimeInMinutes;
        public TravelTime travelTime;
        public TransitTime transitTime;
        public Baggage baggage;
        public List<Object> connectings;
        public boolean facilitiesReady;
        public FacilitiesValue facilitiesValue;
        public Facilities facilities;
        public String operatingFlightNumber;
        public boolean bundlingMeal;
        public boolean bundlingBaggage;
        public List<String> tags;
        public List<String> facilityTags;
        public Object benefits;
        public Info info;
        public Airline airline;
        public OperatingAirline operatingAirline;
    }

    public class Aircraft{
        public Object type;
        public Object seat;
        public Object icon;
        public Object value;
    }

    public static class DepartureDetail{
        public String date;
        public String time;
        public double timezone;
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String terminal;
        public String countryCode;
        public boolean visaRequired;
        public double timeZoneDaylightSaving;
    }

    public static class ArrivalDetail{
        public String date;
        public String time;
        public double timezone;
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String terminal;
        public String countryCode;
        public boolean visaRequired;
        public double timeZoneDaylightSaving;
    }

    public static class TravelTime{
        public int day;
        public int hour;
        public int minute;
    }

    public static class TransitTime{
        public int day;
        public int hour;
        public int minute;
    }

    public static class Baggage{
        public Cabin cabin;
        public CheckIn checkIn;
        public boolean additionalBaggage;
        public String urlIcon;
        public String name;
        public String description;
        public String url;
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

    public class Facilities{
        public Meal meal;
        public AverageDepartureDelayTime averageDepartureDelayTime;
        public Wifi wifi;
        public Usb usb;
        public Entertainment entertainment;
        public Punctuality punctuality;
        public Aircraft aircraft;
        public SeatLayout seatLayout;
    }

    public static class Info{
        public String icon;
        public String title;
        public List<String> descriptions;
    }

    public static class Airline{
        public String code;
        public String name;
        public String displayName;
        public String shortName;
        public Object url;
        public String urlIcon;
    }

    public static class OperatingAirline{
        public String code;
        public String name;
        public String displayName;
        public String shortName;
        public Object url;
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

    public static class Meal{
        public String icon;
        public boolean value;
        public String name;
        public String description;
        public String url;
    }

    public static class AverageDepartureDelayTime{
        public Object icon;
        public Object value;
    }

    public static class Wifi{
        public String icon;
        public boolean value;
        public String name;
        public String description;
        public String url;
    }

    public static class Usb{
        public String icon;
        public boolean value;
        public String name;
        public String description;
        public String url;
    }

    public static class Entertainment{
        public String icon;
        public boolean value;
        public String name;
        public String description;
        public String url;
    }

    public static class Punctuality{
        public Object icon;
        public Object value;
    }

    public static class SeatLayout{
        public Object icon;
        public Object value;
    }

    public static class ReturnFlight{
        public String flightId;
        public String flightIdGeneral;
        public String lyraKey;
        public String flightIdLyra;
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
        public boolean refundable;
        public String journeySellKey;
        public String fareSellKey;
        public boolean roundTrip;
        public String cabinClass;
        public boolean withInsurance;
        public String marketingAirlineCode;
        public FareDetail fareDetail;
        public String fareId;
        public String noShowFeeRefund;
        public String noShowFeeReschedule;
        public String nudgesV2;
        public boolean virtualInterlining;
        public Object corporatePolicy;
        public ArrayList<String> facilitiesPriority;
        public ArrayList<Refund> refund;
        public ArrayList<Reschedule> reschedule;
        public ArrayList<Object> schedulePolicies;
        public ArrayList<String> tags;
        public ArrayList<String> groupTags;
        public ArrayList<String> facilityTags;
        public ArrayList<Schedule> schedules;
        public PenaltyFeeDescriptions penaltyFeeDescriptions;
        public LabelTags labelTags;
        public String airlineName;
    }
}
