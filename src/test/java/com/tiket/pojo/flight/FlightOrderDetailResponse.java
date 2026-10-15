package com.tiket.pojo.flight;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class FlightOrderDetailResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public class Data {
        public String id;
        public String version;
        public long createdDate;
        public String createdBy;
        public Object updatedDate;
        public Object updatedBy;
        public String storeId;
        public int isDeleted;
        public String channelId;
        public Object username;
        public Object ipAddress;
        public Object encryptionType;
        public boolean piiEncryptionSuccess;
        public String itineraryType;
        public String flightDetailType;
        public String fullName;
        public String orderHashId;
        public String email;
        public Object emailEncrypted;
        public String status;
        public String paymentStatus;
        public String origin;
        public String destination;
        public String departureDate;
        public String departureAirline;
        public Object returnAirline;
        public Object returnDate;
        public int totalAdult;
        public int totalChild;
        public int totalInfant;
        public Contact contact;
        public List<Pax> paxes;
        public Object insuranceInfo;
        public List<MultiInsuranceInfo> multiInsuranceInfo;
        public String cartCurrency;
        public String bookingCurrency;
        public List<BookingInformation> bookingInformations;
        public String fareSellKey;
        public String journeySellKey;
        public String sessionGroup;
        public String sessionGroupType;
        public long bookingExpired;
        public long paymentExpired;
        public Object paymentExpiredBuffered;
        public String cartId;
        public String orderId;
        public String orderHash;
        public String paymentUrl;
        public List<FlightItemGroup> flightItemGroups;
        public List<BookResponse> bookResponses;
        public Object bookSsrFeeWithAdjustments;
        public OrderFare orderFare;
        public String resellerId;
        public String sourceOrder;
        public Object bookingAddons;
        public OrderBaggageInfo orderBaggageInfo;
        public OrderMealInfo orderMealInfo;
        public OrderSeatInfo orderSeatInfo;
        public BundlingAddonsInformation bundlingAddonsInformation;
        public List<Object> additionalFees;
        public OrderDetailInformation orderDetailInformation;
        public String lang;
        public List<MultiPax> multiPaxes;
        public Object loginMedia;
        public boolean verifiedPhoneNumber;
        public List<InsuranceOrderRequest> insuranceOrderRequests;
        public String orderType;
        public List<MultiInsurance> multiInsurances;
        public List<BundlingFacilityRule> bundlingFacilityRules;
        public Object orderRefIdUpdateRequester;
        public String resellerType;
        public Object resellerName;
        public Object claimVouchers;
        public Object insuranceData;
        public Object multiCurrency;
        public Object currency;
        public Object additionalFeeOrderRequest;
        public Object appVersion;
        public String fullRefundCredentialCode;
        public Object metasearchRequestId;
        public Object paymentNudges;
        public Object orderNudges;
        public Object promoDataBookingRequest;
        public Object promoDataCartRequest;
        public Object newInclusiveBaggage;
        public Object mapBookingCodeAdditionalData;
        public Object penaltyInfoMap;
        public Object corporateDataRequest;
        public Object rebookingInfo;
        public Object eventTermAndConditionResponses;
        public Object multiInsuranceRequest;
        public boolean dcVar;
        public List<ChurningOrderInformation> churningOrderInformation;
        public Object deviceId;
        public boolean eligibleDownPay;
        public Object eventTermAndConditionRule;
        public Object updatedBookInfo;
        public int totalFare;
        public int totalFareWithInsuranceWithBaggage;
        public int totalFareWithoutInsuranceWithBaggage;
        public int totalFareWithoutInsuranceWithoutBaggage;
        public int totalOldFare;
        public int totalInsurance;
        public int totalBaggageDeparture;
        public int totalBaggageReturn;
        public int totalMealDeparture;
        public int totalMealReturn;
        public int totalSeatDeparture;
        public int totalSeatReturn;
        public int totalBundlingAddonsDeparture;
        public int totalBundlingAddonsReturn;
        public Object cartTotalFares;
        public Object fares;
        public Object oldFares;
        public List<IssuedDataInformation> issuedDataInformations;
        public Object orderRetrieveRescheduleInfo;
        public List<AdjustmentOrder> adjustmentOrders;
        public boolean doubleBooking;
        public boolean equalFare;
        public boolean equalPenaltyInfoFare;
        public boolean enableCancelAndDuplicateInsurance;
        public boolean fullRefund;
        public boolean rebooking;
        public boolean enableMultiCurrencyBooking;
        public boolean orderSyncMonolith;
        public boolean cancelable;
        public boolean deletedAccount;
        public boolean piiencrypted;
        public boolean isFullRefund;
        public PriceAbsorptionData priceAbsorptionData;
        public boolean isPartialPaid;
    }

    public class Contact {
        public String profileId;
        public String firstName;
        public String lastName;
        public String dob;
        public String phoneCode;
        public String phoneNumber;
        public String title;
        public String gender;
        public String email;
        public String emailPassenger;
        public String emailAgent;
        public String accountId;
        public String originalFirstName;
        public String originalLastName;
    }

    public class Pax {
        public int paxNumber;
        public String profileId;
        public String type;
        public String title;
        public String gender;
        public String firstName;
        public String lastName;
        public String originalFirstName;
        public String originalLastName;
        public String nationality;
        public String dob;
        public Object phoneCode;
        public Object phoneNumber;
        public IdentityNumber identityNumber;
        public Object travelPurpose;
        public Passport passport;
        public Baggage baggage;
        public Meals meals;
        public Seats seats;
        public String frequentFlyer;
        public boolean insurance;
    }

    public class IdentityNumber {
        public String cipherText;
        public String type;
    }

    public class Passport {
        public Object id;
        public Object expired;
        public Object country;
        public Object issuingDate;
    }

    public class Baggage {
        public int departureBaggage;
        public int returnBaggage;
        public List<BaggageDetail> departureBaggages;
        public List<BaggageDetail> returnBaggages;
    }

    public class BaggageDetail {
        public String unitOfMeasure;
        public int weight;
    }

    public class Meals {
        public List<Object> departureMeals;
        public List<Object> returnMeals;
    }

    public class Seats {
        public List<Object> departureSeats;
        public List<Object> returnSeats;
    }

    public class MultiInsuranceInfo {
        public boolean insurance;
        public int pricePerPax;
        public int compensationAmount;
        public int priceTotal;
        public String currency;
        public String code;
    }

    public class BookingInformation {
        public Account account;
        public String distributionType;
        public String bookingCode;
        public Object bookingCodeAirlines;
        public String bookingStatus;
        public Object invoiceNumber;
        public Object logId;
        public String countryId;
        public boolean issuedSyncMonolith;
        public Object transactionId;
        public Object additionalData;
        public String bookingDate;
        public String issuedDate;
    }

    public class Account {
        public String name;
        public String code;
    }

    public class FlightItemGroup {
        public String origin;
        public String destination;
        public String departureDate;
        public Object returnDate;
        public List<ItemDetail> itemDetails;
        public List<FareDetail> fareDetails;
        @JsonProperty("fareDetailBreakdown")
        public List<FareDetail> fareDetailBreakdown; // Re-using FareDetail structure
        public List<Refund> refund;
        public List<Reschedule> reschedule;
        public PenaltyFeeDescriptions penaltyFeeDescriptions;
        public Object tagProperties;
        public Object brandedName;
        public Object corporate;
        public List<OrderItemAttribute> orderItemAttributes;
        public boolean multiFare;
    }

    public class ItemDetail {
        public String journeyType;
        public String flightId;
        public String fareBasisCodeSearch;
        public String flightSelect;
        public String currency;
        public String origin;
        public String destination;
        public Object date;
        public Airline marketingAirline;
        public int adult;
        public int child;
        public int infant;
        public Departure departure;
        public Arrival arrival;
        public Object fareDetail;
        public int totalTravelTimeInMinutes;
        public int totalTransitTimeInMinutes;
        public Object totalTravelTime;
        public Object totalTransitTime;
        public int totalTransit;
        public int seatAvailability;
        public List<Schedule> schedules;
        public List<String> facilitiesPriority;
        public boolean holdable;
        public boolean upgradable;
        public boolean refundable;
        public boolean international;
        public String fareSellKey;
        public String journeySellKey;
        public String marriageGroup;
        public boolean roundTrip;
        public String cabinClass;
        public List<String> tags;
        public long cacheTimestamp;
        public int fareRankAll;
        public int fareRankAirline;
        public int departRankEarliestAll;
        public int departRankEarliestAirline;
        public int departRankLatestAll;
        public int departRankLatestAirline;
        public CacheDetail cacheDetail;
        public boolean priceChange;
        public boolean crossFare;
    }

    public class Airline {
        public String code;
        public String name;
        @JsonProperty("short_name")
        public String shortName;
        @JsonProperty("display_name")
        public String displayName;
        @JsonProperty("icon_url")
        public String iconUrl;
    }

    public class Departure {
        public String date;
        public String time;
        public int timezone;
        public int timeZoneDaylightSaving;
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String terminal;
        public String countryCode;
        public boolean visaRequired;
    }

    public class Arrival {
        public String date;
        public String time;
        public int timezone;
        public int timeZoneDaylightSaving;
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String terminal;
        public String countryCode;
        public boolean visaRequired;
    }

    public class Schedule {
        public String fareClass;
        public Airline airline;
        public String flightNumber;
        public Aircraft aircraft;
        public String cabinClass;
        public DepartureDetail departureDetail;
        public ArrivalDetail arrivalDetail;
        public TravelTime travelTime;
        public int totalTravelTimeInMinutes;
        public int totalTransitTimeInMinutes;
        public Object transitTime;
        public ScheduleBaggage baggage;
        public List<BaggagePerPax> baggagePerPax;
        public List<Object> connectings;
        public boolean facilitiesReady;
        public Facilities facilities;
        public Object marriageGroup;
        public Airline operatingAirline;
        public String operatingFlightNumber;
        public boolean bundlingMeal;
        public boolean bundlingBaggage;
        public FacilitiesValue facilitiesValue;
        public List<String> tags;
        public List<Object> benefits;
        public Object info;
        public Object infos;
    }

    public class Aircraft {
        public String type;
        public Object seat;
    }

    public class DepartureDetail {
        public String date;
        public String time;
        public int timezone;
        public int timeZoneDaylightSaving;
        public String airportCode;
        public String terminal;
        public String airportName;
        public String cityCode;
        public String cityName;
        public boolean visaRequired;
    }

    public class ArrivalDetail {
        public String date;
        public String time;
        public int timezone;
        public int timeZoneDaylightSaving;
        public String airportCode;
        public String terminal;
        public String airportName;
        public String cityCode;
        public String cityName;
        public boolean visaRequired;
    }

    public class TravelTime {
        public int day;
        public int hour;
        public int minute;
    }

    public class ScheduleBaggage {
        public Cabin cabin;
        public CheckIn checkIn;
        public boolean additionalBaggage;
        public String urlIcon;
    }

    public class Cabin {
        public int unit;
        public String measurement;
        public int qty;
        public String desc;
    }

    public class CheckIn {
        public int unit;
        public String measurement;
        public int qty;
        public String desc;
    }

    public class BaggagePerPax {
        public String paxType;
        public Cabin cabin;
        public CheckIn checkIn;
    }

    public class Facilities {
        public FacilityDetail meal;
        public FacilityDetail averageDepartureDelayTime;
        public FacilityDetail wifi;
        public FacilityDetail usb;
        public FacilityDetail entertainment;
        public FacilityDetail punctuality;
        public FacilityDetail aircraft;
        public FacilityDetail seatLayout;
    }

    public class FacilityDetail {
        public String icon;
        public Object value;
    }

    public class FacilitiesValue {
        public String meal;
        public Object averageDepartureDelayTime;
        public String wifi;
        public String usb;
        public String entertainment;
        public Object punctuality;
        public String aircraft;
        public Object seatLayout;
    }

    public class CacheDetail {
        public int ttl;
        public long cacheTimestamp;
        public long cacheExpired;
        public long cacheRuleExpired;
        public CacheRule cacheRule;
    }

    public class CacheRule {
        public int startSearchDate;
        public int endSearchDate;
        public String origin;
        public String destination;
        public String startDepartureDate;
        public String endDepartureDate;
        public String airline;
        public String adult;
        public String child;
        public String infant;
        public String status;
        public int ttl;
        public String channelId;
        public Object id;
    }

    public class FareDetail {
        public String fareBasisCode;
        public String fareClass;
        public List<Fare> fares;
        public int maxRefundHour;
        public boolean refundable;
    }

    public class Fare {
        public String supplierId;
        public String distributionType;
        public String currency;
        public int cheapestFare;
        public FareInfo totalFare;
        public FareInfo adultFare;
        public FareInfo childFare;
        public FareInfo infantFare;
        public List<String> discountLabels;
        public Object journeySellKey;
        public Object fareSellKey;
        public String originalCurrency;
        public FareInfo originalTotalFare;
        public FareInfo originalAdultFare;
        public FareInfo originalChildFare;
        public FareInfo originalInfantFare;
        public String fareClasses;
        public Object rescheduleFare;
        public Object tags;
        public List<String> campaignLabels;
        public Object currencyExchangeRate;
        public Object brandedFare;
    }

    public class FareInfo {
        public long total;
        public long totalWithoutAdjustment;
        public long totalWithMarkupWithoutDiscount;
        public long base;
        public int commission;
        public int iwjr;
        public long tax;
        public int markup;
        public long discount;
        public int seatAvailables;
        public int supplierDiscount;
        public int loyaltyPoint;
        public int insurance;
        public String premiNumber;
        public long totalWithInsurance;
        public List<PenaltyInfo> penaltyInfos;
        public List<String> fareBasisCodes;
        public Object refundInformation;
        public String adjustmentId;
        public String campaignId;
        public List<Object> fareBasisCodeSchedule;
        public int d5;
    }

    public class PenaltyInfo {
        public String penaltyType;
        public String penaltyStatus;
        public int percent;
        public int minutes;
        public String timeFrame;
        public long amount;
        public String currencyCode;
        public int decimalPlaces;
        public long originalAmount;
        public String originalCurrencyCode;
        public int originalDecimalPlaces;
        public String penaltyDescription;
        public boolean cat16Info;
        public Object adultPercentage;
        public Object childPercentage;
        public Object infantPercentage;
        public Object finalAmountAdult;
        public Object finalAmountChild;
        public Object finalAmountInfant;
        public boolean active;
        public boolean eligible;
        public long finalAmount;
        public int feePercentage;
    }

    public class Refund {
        public String desc;
        public long amountAdult;
        public Object amountChild;
        public Object amountInfant;
        public int adultPercentage;
        public Object childPercentage;
        public Object infantPercentage;
        public long finalAmountAdult;
        public Object finalAmountChild;
        public Object finalAmountInfant;
        public boolean active;
        public boolean eligible;
    }

    public class Reschedule {
        public String desc;
        public long amountAdult;
        public Object amountChild;
        public Object amountInfant;
        public int adultPercentage;
        public Object childPercentage;
        public Object infantPercentage;
        public long finalAmountAdult;
        public Object finalAmountChild;
        public Object finalAmountInfant;
        public boolean active;
        public boolean eligible;
    }

    public class PenaltyFeeDescriptions {
        public String reschedule;
        public String refund;
    }

    public class OrderItemAttribute {
        public String key;
        public String value;
    }

    public class BookResponse {
        public MandatoryRequest mandatoryRequest;
        public IntegratorBookRequest integratorBookRequest;
        public BookDetail bookDetail;
    }

    public class MandatoryRequest {
        public String storeId;
        public String channelId;
        public String requestId;
        public String serviceId;
        public String resellerId;
        public String identity;
        public String username;
        public Object loyaltyLevel;
        public Object deviceId;
        public Map<String, Object> experimentVariants;
        @JsonProperty("X-Account-Id")
        public Object xAccountId;
    }

    public class IntegratorBookRequest {
        public String distributionType;
        public Account account;
        public int adult;
        public int child;
        public int infant;
        public Object corporate;
        public Object promoCode;
        public Object viaSelect;
        public String flightSelect;
        public Object fareSellKey;
        public Object journeySellKey;
        public Object sessionGroup;
        public Object sessionGroupType;
        public IntegratorContact contact;
        public List<IntegratorPax> paxes;
        public List<Itinerary> itineraries;
        public String currency;
        public String countryId;
        public int commission;
        public int maxBookRetry;
        public Object additionalData;
        public boolean multiFare;
    }

    public class IntegratorContact {
        public String profileId;
        public String firstName;
        public String lastName;
        public String dob;
        public String phoneCode;
        public String phoneNumber;
        public String title;
        public String gender;
        public String email;
        public String emailPassenger;
        public String emailAgent;
        public Object accountId;
    }

    public class IntegratorPax {
        public int paxNumber;
        public Object profileId;
        public String type;
        public String title;
        public String gender;
        public String firstName;
        public String lastName;
        public String originalFirstName;
        public String originalLastName;
        public String nationality;
        public String dob;
        public Object phoneCode;
        public Object phoneNumber;
        public IdentityNumber identityNumber;
        public Object travelPurpose;
        public Object passport;
        public Baggage baggage;
        public Meals meals;
        public Seats seats;
        public Object frequentFlyer;
        public boolean insurance;
    }

    public class Itinerary {
        public String airline;
        public String departure;
        public String arrival;
        public String date;
        public String connecting;
        public int international;
        public String cabinClass;
        public List<IntegratorSchedule> schedules;
        public Object blacklistMap;
        public Object brandedFare;
    }

    public class IntegratorSchedule {
        public String arrivalDate;
        public String departureDate;
        public OperatingAirline operatingAirline;
        public String fareClass;
        public String origin;
        public String destination;
        public String marketingAirline;
        public String flightNumber;
        public Object marriageGroup;
        public FareBasisCodes fareBasisCodes;
    }

    public class OperatingAirline {
        public String code;
        public String number;
    }

    public class FareBasisCodes {
        public String adult;
        public Object child;
        public Object infant;
        public Object adultBreakpoint;
        public Object childBreakpoint;
        public Object infantBreakpoint;
    }

    public class BookDetail {
        public String distributionType;
        public Account account;
        public List<String> corporateCodes;
        public String bookingCode;
        public String bookingExpired;
        public Object invoiceNumber;
        public Object logId;
        public String currency;
        public String originalCurrency;
        public BookingFare bookingFare;
        public BookingFare originalBookingFare;
        public List<BookDetailItinerary> itineraries;
        public BreakdownInfo passengerFareInfo; // Re-using BreakdownInfo
        public Object transactionId;
        public String bookingDate;
        public Object additionalData;
    }

    public class BookingFare {
        public double balanceDue;
        public double bookedFare;
        public double fare;
        public double adultFare;
        public double childFare;
        public double infantFare;
        public double commission;
        public double commissionPercent;
        public double commissionAddons;
        public double commissionPercentAddons;
        public double nta;
        public double agentFee;
        public SsrFee ssrFee;
        public double incentive;
        public double upselling;
        public double voucher;
        public Object voucherCode;
        public double markup;
        public double privateMarkup;
        public double fareExcludeSsr;
        public double discount;
        public Object discountCode;
    }

    public class SsrFee {
        public int ssrFare;
        public int baggageFare;
        public int mealsFare;
        public int seatsFare;
        public int medicalsFare;
        public int commissionBaggage;
        public int commissionMeals;
        public int commissionSeats;
        public int commissionMedicals;
        public List<SsrItinerary> ssrItineraries;
    }

    public class SsrItinerary {
        public List<SsrPaxMeal> ssrPaxMeals;
        public List<SsrPaxSeat> ssrPaxSeats;
        public List<SsrPaxBaggage> ssrPaxBaggages;
        public Object ssrPaxMedicals;
    }

    public class SsrPaxMeal {
        public int paxNumber;
        public List<Object> ssrMealsLegs;
    }

    public class SsrPaxSeat {
        public int paxNumber;
        public List<Object> ssrSeatLegs;
    }

    public class SsrPaxBaggage {
        public int paxNumber;
        public List<SsrBaggageLeg> ssrBaggagesLegs;
    }

    public class SsrBaggageLeg {
        public String departure;
        public String arrival;
        public String weightType;
        public int weight;
        public int fare;
        public int nta;
        public int commission;
        public boolean success;
    }

    public class BookDetailItinerary {
        public int international;
        public String currency;
        public String originalCurrency;
        public long fare;
        public long adultFare;
        public int childFare;
        public int infantFare;
        public long originalFare;
        public long originalAdultFare;
        public int originalChildFare;
        public int originalInfantFare;
        public int adult;
        public int child;
        public int infant;
        public String cabinClass;
        public List<BookDetailSchedule> schedules;
        public BreakdownInfo breakdownInfo;
    }

    public class BookDetailSchedule {
        public String departureDate;
        public String arrivalDate;
        public OperatingAirline operatingAirline;
        public String departureTerminal;
        public String arrivalTerminal;
        public FareBasisCodes fareBasisCodes;
        public String fareBasisCode;
        public String fareClass;
        public String origin;
        public String destination;
        public String marketingAirline;
        public String marketingFlightNumber;
        public ScheduleBaggageInfo baggage;
        public String stopQuantity;
        public String aircraftType;
        public List<Object> connectings;
        public int isPromo;
    }

    public class ScheduleBaggageInfo {
        public SimpleBaggageDetail cabin;
        public CheckInBaggageDetail checkIn;
    }

    public class SimpleBaggageDetail {
        public Object unit;
        public Object measurement;
        public int qty;
        public boolean validateBaggage;
    }

    public class CheckInBaggageDetail {
        public Object unit;
        public String measurement;
        public int qty;
        public boolean validateBaggage;
    }

    public class BreakdownInfo {
        public PaxFareDetail adult;
        public PaxFareDetail child;
        public PaxFareDetail infant;
        public int baggage;
        public PaxFareDetail originalAdult;
        public PaxFareDetail originalChild;
        public PaxFareDetail originalInfant;
        public int originalBaggage;
    }

    public class PaxFareDetail {
        public double psc;
        public double iwjr;
        public double discount;
        public double baseFare;
        public double originalBaseFare;
        public double nta;
        public double commission;
        public double fare;
        public double tax;
        public double agentFee;
        public double markup;
        public double privateMarkup;
        public List<BreakdownTax> breakdownTaxes;
    }

    public class BreakdownTax {
        public String code;
        public String currencyCode;
        public int amount;
    }

    public class OrderFare {
        public List<BookDetailItinerary> bookItinerariesFare;
        public List<BookingFare> bookingFares;
        public List<BookingFare> originalBookingFares;
        public List<FareInfo> cartTotalFare;
        public List<Commission> commissions;
        public List<Object> originalCommissions;
        public CalculatedValue calculatedValue;
        public boolean equalFare;
        public boolean equalPenaltyInfoFare;
    }

    public class Commission {
        public String bookingCode;
        public double commissionAdult;
        public double commissionAdultPercentage;
        public Object commissionTypesAdult;
        public double commissionValueAdult;
        public double commissionChild;
        public double commissionChildPercentage;
        public Object commissionTypesChild;
        public double commissionValueChild;
        public double commissionInfant;
        public double commissionInfantPercentage;
        public Object commissionTypesInfant;
        public double commissionValueInfant;
        public double commissionTotal;
        public String commissionTypeAdult;
        public String commissionTypeChild;
        public String commissionTypeInfant;
        public Object commissionTourCode;
    }

    public class CalculatedValue {
        public double totalAdjustmentRevenue;
        public double totalMarkup;
        public double totalPrivateMarkup;
        public double totalDiscount;
        public double totalRevenue;
        public double totalCommission;
    }

    public class OrderBaggageInfo {
        public int totalDeparture;
        public int totalReturn;
        public List<Object> baggageResponses;
    }

    public class OrderMealInfo {
        public int totalDeparture;
        public int totalReturn;
        public List<MealsResponse> mealsResponses;
    }

    public class MealsResponse {
        public Object mandatoryRequest;
        public Object integratorMealsRequest;
        public Object mealsDetailResponse;
        public Object timeRule;
        public Object mealRules;
    }

    public class OrderSeatInfo {
        public int totalDeparture;
        public int totalReturn;
    }

    public class BundlingAddonsInformation {
        public Object adultBundlingAddons;
        public Object childBundlingAddons;
        public Object infantBundlingAddons;
        public int totalDeparturePrice;
        public int totalDepartureNetPrice;
        public int totalReturnPrice;
        public int totalReturnNetPrice;
    }

    public class OrderDetailInformation {
        public List<OrderDetailId> orderDetailIds;
    }

    public class OrderDetailId {
        @JsonProperty("order_detail_id")
        public String orderDetailId;
        @JsonProperty("booking_no")
        public String bookingNo;
        @JsonProperty("flight_id")
        public String flightId;
        public String type;
        public List<OrderDetailPassenger> passengers;
    }

    public class OrderDetailPassenger {
        public int paxNumber;
        @JsonProperty("first_name")
        public String firstName;
        @JsonProperty("last_name")
        public String lastName;
        @JsonProperty("original_first_name")
        public String originalFirstName;
        @JsonProperty("original_last_name")
        public String originalLastName;
        public String title;
        public Object gender;
        @JsonProperty("order_passenger_id")
        public String orderPassengerId;
    }

    public class MultiPax {
        public String origin;
        public String destination;
        public String bookingCode;
        public String journeyType;
        public List<Pax> listPax; // Re-using Pax
    }

    public class InsuranceOrderRequest {
        public String booking_code;
        public String flight_type;
        public List<Object> products;
        public TotalMap total_map;
    }

    public class TotalMap {
        @JsonProperty("COMMISSION")
        public int commission;
        @JsonProperty("PRICE")
        public int price;
        @JsonProperty("FEE")
        public int fee;
        @JsonProperty("DISCOUNT")
        public int discount;
    }

    public class MultiInsurance {
        public boolean insurance;
        public int pricePerPax;
        public int compensationAmount;
        public int priceTotal;
        public String currency;
        public String code;
        public Object content;
        public String url;
        public int insuranceCount;
        public DescriptionContent descriptionContent;
        public String descriptionIcon;
        public Object confirmation;
        public String tnc;
        public String howToClaim;
        public PriceDetail priceDetail;
        public Object additionalAddonsInsurance;
        public String template;
        public List<AdditionalContent> additionalContents;
        public List<CredentialRule> credentialRule;
        public String credentialCode;
        public String referenceDetail;
        public String bookingCode;
        public List<DescriptionContent> descriptionContents;
        public Object contentTitle;
        public String title;
        public String contentOfDrawerHtml;
    }

    public class DescriptionContent {
        public String lang;
        public String content;
        public String contentOfDrawer;
        public String name;
    }

    public class PriceDetail {
        public int adult;
        public int child;
        public int infant;
    }

    public class AdditionalContent {
        public String lang;
        public Object productName;
        public Object productNameColourFont;
        public String productIcon;
        public String productLogo;
        public String productDescription;
        public String productDescription2;
        public String eligibleRefundPage;
        public String eligibleRefundPageDetail;
        public String nonEligibleRefundPage;
        public String nonEligibleRefundPageDetail;
        public String tncTitle;
        public String tncContent;
        public String nonEligibleReschedulePage;
        public String nonEligibleReschedulePageDetail;
        public String eligibleReschedulePage;
        public String eligibleReschedulePageDetail;
        public String productDescriptionReschedule;
        public String productIconReschedule;
        public String productLogoReschedule;
        public String tncContentReschedule;
        public String tncTitleReschedule;
        public Object disableIcon;
        public Object shortDescription;
    }

    public class CredentialRule {
        public String parameter;
        public String value;
        public String arithmetics;
    }

    public class BundlingFacilityRule {
        public String origin;
        public String destination;
        public List<BundlingFacility> bundlingFacilities;
        public Period departurePeriods;
        public Period issuancePeriods;
    }

    public class BundlingFacility {
        public String type;
        public String howToUseUrl;
        public String description;
        public String applicability;
        public boolean sendGiftVoucher;
        public Object giftVoucher;
        public TextValue headerText;
        public TextValue shortDescriptions;
        public Object termsConditions;
        public boolean sendToEticket;
        public TextValue listText;
        public String listIcon;
        public Object disabledListIcon;
    }

    public class TextValue {
        @JsonProperty("ID")
        public String id;
        @JsonProperty("EN")
        public String en;
    }

    public class Period {
        public String startDate;
        public String endDate;
    }

    public class ChurningOrderInformation {
        public String bookingCode;
        public boolean nonHold;
        public boolean isNonHold;
    }

    public class IssuedDataInformation {
        public Object insuranceAllowanceRefund;
        public Object passengerOrderRequest;
        public OrderTicketData orderTicketData;
        public String bookingCode;
        public Object bookingQrCode;
        public Object bookResponses;
        public Object penaltyInfoRefund;
        public Object voucherRules;
        public Object adjustmentOrders;
        public Object insuranceData;
    }

    public class OrderTicketData {
        public String issuedStatus;
        public String bookingCode;
        public Object bookingQrCode;
        public String distributionType;
        public String supplier;
        public int paymentAmount;
        public Object oldTicketSource;
        public List<PaxResponse> paxResponses;
        public Object ticketType;
        public Object pdfBase64;
        public Object sessionId;
        public Object bookingCodeSabres;
        public String actionKey;
    }

    public class PaxResponse {
        public String name;
        public Object originalPaxName;
        public String ticketNumber;
        public Object ticketSource;
        public Object addons;
        public Object pricingDetails;
        public Object originalPricingDetails;
        public int pqrNumber;
        public int paxNumber;
        public Object type;
        public Object title;
    }

    public class AdjustmentOrder {
        public String id;
        public int version;
        public long createdDate;
        public String createdBy;
        public long updatedDate;
        public String updatedBy;
        public String storeId;
        public int isDeleted;
        public String channelId;
        public String username;
        public Object ipAddress;
        public String orderRefId;
        public String bookingCode;
        public String status;
        public long total;
        public int totalDiscount;
        public int totalMarkup;
        public List<AdjustmentItinerary> adjustmentItinerariesAdult;
        public List<AdjustmentItinerary> adjustmentItinerariesChild;
        public List<Object> adjustmentItinerariesInfant;
        public List<DiscountLabel> discountLabels;
        public long bookingExpired;
        public long paymentExpired;
        public int totalCashback;
        public boolean kuber;
        public boolean priceAbsorb;
    }

    public class AdjustmentItinerary {
        public long total;
        public int markup;
        public int discount;
        public String adjustmentId;
        public String campaignId;
        public String transactionId;
        public List<String> discountLabels;
        public DiscountDetail discountDetail;
        public int cashback;
    }

    public class DiscountDetail {
        public int total;
        public String paxType;
        public List<Breakdown> breakdowns;
        public int cashback;
    }

    public class Breakdown {
        public String code;
        public int value;
        public boolean includedInPrice;
        public boolean displayLabel;
        public String campaignId;
        public String adjustmentId;
        public String type;
    }

    public class DiscountLabel {
        public String type;
        public String headerText;
        public String headerDescription;
        public String totalText;
        public String headerIcon;
        public String listIcon;
        public String discountLabelType;
    }

    public class PriceAbsorptionData {
        public List<PriceBreakdownData> priceBreakdownData;
    }

    public class PriceBreakdownData {
        public boolean priceAbsorbed;
        public String bookingCode;
        public String currency;
        public String origin;
        public String destination;
        public Object journeyType;
        public Object flightId;
        public Object cartFareDetail;
        public Object bookFareDetail;
        public Object differencePriceDetail;
    }
}
