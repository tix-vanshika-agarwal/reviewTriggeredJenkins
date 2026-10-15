package com.tiket.pojo.flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class FlightGetCartDetail {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public Date serverTime;
    
public class AddonConfigs{
    @JsonProperty("BAGGAGE") 
    public boolean bAGGAGE;
    @JsonProperty("SEAT") 
    public boolean sEAT;
    @JsonProperty("MEAL") 
    public boolean mEAL;
    @JsonProperty("BUNDLING_ADDONS") 
    public boolean bUNDLING_ADDONS;
    @JsonProperty("INSURANCES") 
    public boolean iNSURANCES;
}

public class AddonsForm{
    public BaggagePax baggagePax;
    public MealPax mealPax;
    public SeatPax seatPax;
    public BundlingAddonsPax bundlingAddonsPax;
}

public class AdultFare{
    public int total;
    public int totalWithoutAdjustment;
    public int totalWithMarkupWithoutDiscount;
    public int base;
    public int commission;
    public int iwjr;
    public int tax;
    public int markup;
    public int discount;
    public DiscountDetail discountDetail;
    public int seatAvailables;
    public int supplierDiscount;
    public int loyaltyPoint;
    public int insurance;
    public Object premiNumber;
    public int totalWithInsurance;
    public ArrayList<PenaltyInfo> penaltyInfos;
    public Object fareBasisCodes;
    public Object refundInformation;
    public String adjustmentId;
    public String campaignId;
    public int d5;
}

public class AdultFormSection{
    public String sectionName;
    public String sectionInfo;
    public ArrayList<Form> forms;
}

public class Aircraft{
    public String type;
    public Object seat;
    public Object icon;
    public Object value;
}

public class Airline{
    public String code;
    public String name;
    public String shortName;
    public String displayName;
    public String urlIcon;
}

public class Arrival{
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

public class ArrivalDetail{
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

public class AverageDepartureDelayTime{
    public Object icon;
    public Object value;
}

public class Baggage{
    public Cabin cabin;
    public CheckIn checkIn;
    public boolean additionalBaggage;
    public String urlIcon;
    public String name;
    public String url;
    public String description;
}

public class BaggagePax{
    public Object url;
    public ArrayList<Object> adultBaggage;
    public ArrayList<Object> childBaggage;
}

public class BaggagePerPax{
    public String paxType;
    public Cabin cabin;
    public CheckIn checkIn;
}

public class Breakdown{
    public String code;
    public int value;
    public boolean includedInPrice;
    public boolean displayLabel;
    public Object campaignId;
    public Object adjustmentId;
}

public class BundlingAddonsPax{
    public ArrayList<Object> adultAddons;
    public ArrayList<Object> childAddons;
    public ArrayList<Object> infantAddons;
    public Object bundlingAddonsMap;
    public Object url;
    public Object title;
    public Object content;
    public Object icon;
}

public class BundlingFacility{
    public String type;
    public String howToUseUrl;
    public String description;
    public String applicability;
    public boolean sendGiftVoucher;
    public GiftVoucher giftVoucher;
    public HeaderText headerText;
    public ShortDescriptions shortDescriptions;
    public Object termsConditions;
    public boolean sendToEticket;
}

public class BundlingFacilityRule{
    public String origin;
    public String destination;
    public ArrayList<BundlingFacility> bundlingFacilities;
    public DeparturePeriods departurePeriods;
    public IssuancePeriods issuancePeriods;
}

public class Cabin{
    public int unit;
    public String measurement;
    public int qty;
    public String desc;
}

public class CacheDetail{
    public int ttl;
    public Date cacheTimestamp;
    public Date cacheExpired;
    public Date cacheRuleExpired;
    public CacheRule cacheRule;
}

public class CacheRule{
    public int startSearchDate;
    public int endSearchDate;
    public Object origin;
    public Object destination;
    public Object startDepartureDate;
    public Object endDepartureDate;
    public Object airline;
    public Object adult;
    public Object child;
    public Object infant;
    public Object status;
    public int ttl;
    public Object channelId;
    public Object id;
}

public class CheckIn{
    public int unit;
    public String measurement;
    public int qty;
    public String desc;
}

public class ChildFare{
    public int total;
    public int totalWithoutAdjustment;
    public int totalWithMarkupWithoutDiscount;
    public int base;
    public int commission;
    public int iwjr;
    public int tax;
    public int markup;
    public int discount;
    public Object discountDetail;
    public int seatAvailables;
    public int supplierDiscount;
    public int loyaltyPoint;
    public int insurance;
    public Object premiNumber;
    public int totalWithInsurance;
    public ArrayList<Object> penaltyInfos;
    public Object fareBasisCodes;
    public Object refundInformation;
    public String adjustmentId;
    public String campaignId;
    public int d5;
}

public class ContactForm{
    public String sectionId;
    public String name;
    public Object fieldName;
    public String placeholder;
    public String caption;
    public String title;
    public String type;
    public ArrayList<Validator> validators;
    public String referenceField;
    public String inputSourceURL;
    public ArrayList<InputSource> inputSources;
}

public class ContactSegment{
    public ArrayList<ContactForm> contactForm;
}

public class Coverage{
    public String type;
    public String title;
    public String currency;
    public int minRange;
    public int maxRange;
    public String minRangeFormatted;
    public String maxRangeFormatted;
    public String additionalInfo;
}

public class CredentialRule{
    public String parameter;
    public String value;
    public String arithmetics;
}

public class Data{
    public String status;
    public String channelId;
    public String resellerType;
    public Object userName;
    public Insurance insurance;
    public ArrayList<MultiInsurance> multiInsurances;
    public ArrayList<Detail> details;
    public ContactSegment contactSegment;
    public PassengerForm passengerForm;
    public PassengerFormSection passengerFormSection;
    public AddonsForm addonsForm;
    public Object optionData;
    public int bookingTimeLimit;
    public int bookingTimeLimitInSeconds;
    public int bookingTimeLeftInSeconds;
    public DeparturePriceDetail departurePriceDetail;
    public Object returnPriceDetail;
    public Policy policy;
    public GeneralIcons generalIcons;
    public InclusiveInformation inclusiveInformation;
    public ArrayList<Object> additionalLabelProperties;
    public ArrayList<DiscountLabelProperty> discountLabelProperties;
    public EventTermAndConditionResponses eventTermAndConditionResponses;
    public RefundProperties refundProperties;
    public RescheduleProperties rescheduleProperties;
    public ArrayList<Object> additionalFees;
    public String cartType;
    public Object rescheduleAmount;
    public Object rescheduleForm;
    public ArrayList<TagProperty> tagProperties;
    public ArrayList<BundlingFacilityRule> bundlingFacilityRules;
    public Object fees;
    public String flightType;
    public MultiCurrency multiCurrency;
    public Object flightPriceLabelProperties;
    public AddonConfigs addonConfigs;
    public ArrayList<OrderNudge> orderNudges;
    public String refundTrackerFlag;
    public String rescheduleTrackerFlag;
    public PaymentNudges paymentNudges;
    public boolean fullRefund;
    public boolean insuranceAvailable;
    public boolean isFullRefund;
}

public class Departure{
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

public class DepartureDetail{
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

public class DepartureInclusive{
    public String origin;
    public String destination;
    public Baggage baggage;
    public String flightId;
    public String departureDate;
}

public class DeparturePeriods{
    public String startDate;
    public String endDate;
}

public class DeparturePriceDetail{
    public int totalAdult;
    public int totalAdultWithoutAdjustment;
    public int totalAdultWithMarkupWithoutDiscount;
    public int totalChild;
    public int totalChildWithoutAdjustment;
    public int totalChildWithMarkupWithoutDiscount;
    public int totalInfant;
    public int totalInfantWithoutAdjustment;
    public int totalInfantWithMarkupWithoutDiscount;
    public int totalWithInsuranceAllowance;
    public int totalAdultWithInsuranceAllowance;
    public int totalChildWithInsuranceAllowance;
    public int totalInfantWithInsuranceAllowance;
}

public class DescriptionContent{
    public String lang;
    public String content;
    public String contentOfDrawer;
    public String name;
}

public class Detail{
    public String origin;
    public String destination;
    public String departureDate;
    public Object returnDate;
    public ArrayList<ItemDetail> itemDetails;
    public ArrayList<FareDetail> fareDetails;
    public ArrayList<FareDetailBreakdown> fareDetailBreakdown;
    public ArrayList<Refund> refund;
    public ArrayList<Reschedule> reschedule;
    public PenaltyFeeDescriptions penaltyFeeDescriptions;
    public Object accountCode;
    public Object corporate;
    public ArrayList<OrderItemAttribute> orderItemAttributes;
}

public class DiscountDetail{
    public int total;
    public ArrayList<Breakdown> breakdowns;
}

public class DiscountLabelProperty{
    public String type;
    public String headerText;
    public String headerDescription;
    public String totalText;
    public String headerIcon;
    public String listIcon;
    public String discountLabelType;
}

public class Entertainment{
    public String icon;
    public boolean value;
    public String name;
    public String url;
    public String description;
}

public class EventTermAndCondition{
    @JsonProperty("RESCHEDULE_WITHOUT_INSURANCE_INFO_V4") 
    public RESCHEDULEWITHOUTINSURANCEINFOV4 rESCHEDULE_WITHOUT_INSURANCE_INFO_V4;
    @JsonProperty("RESCHEDULE_WITHOUT_INSURANCE") 
    public RESCHEDULEWITHOUTINSURANCE rESCHEDULE_WITHOUT_INSURANCE;
    @JsonProperty("RESCHEDULE_WITHOUT_INSURANCE_V4") 
    public RESCHEDULEWITHOUTINSURANCEV4 rESCHEDULE_WITHOUT_INSURANCE_V4;
    @JsonProperty("REFUND_WITHOUT_INSURANCE_INFO_V4") 
    public REFUNDWITHOUTINSURANCEINFOV4 rEFUND_WITHOUT_INSURANCE_INFO_V4;
    @JsonProperty("REFUND_WITHOUT_INSURANCE") 
    public REFUNDWITHOUTINSURANCE rEFUND_WITHOUT_INSURANCE;
    @JsonProperty("REFUND_WITHOUT_INSURANCE_V4") 
    public REFUNDWITHOUTINSURANCEV4 rEFUND_WITHOUT_INSURANCE_V4;
}

public class EventTermAndConditionResponses{
    public Reschedule reschedule;
    public Refund refund;
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

public class FacilitiesValue{
    public String meal;
    public Object averageDepartureDelayTime;
    public String wifi;
    public String usb;
    public String entertainment;
    public Object punctuality;
    public String aircraft;
    public Object seatLayout;
}

public class Fare{
    public String distributionType;
    public String supplierId;
    public Object journeySellKey;
    public Object fareSellKey;
    public String currency;
    public int cheapestFare;
    public TotalFare totalFare;
    public AdultFare adultFare;
    public ChildFare childFare;
    public InfantFare infantFare;
    public ArrayList<String> discountLabels;
    public String fareClasses;
    public String originalCurrency;
    public OriginalTotalFare originalTotalFare;
    public OriginalAdultFare originalAdultFare;
    public OriginalChildFare originalChildFare;
    public OriginalInfantFare originalInfantFare;
    public Object penaltyFeeDescriptions;
    public Object rescheduleFare;
    public ArrayList<String> tags;
    public ArrayList<String> campaignLabels;
}

public class FareDetail{
    public Object fareBasisCode;
    public String fareClass;
    public ArrayList<Fare> fares;
}

public class FareDetailBreakdown{
    public Object fareBasisCode;
    public String fareClass;
    public ArrayList<Fare> fares;
}

public class Form{
    public String name;
    public String placeholder;
    public String caption;
    public String title;
    public String type;
    public ArrayList<Validator> validators;
    public String referenceField;
    public String inputSourceURL;
    public ArrayList<InputSource> inputSources;
}

public class GeneralIcons{
    public Meal meal;
    public RefundYes refundYes;
    public RescheduleYes rescheduleYes;
    public Nobaggage nobaggage;
    public Wifi wifi;
    public Baggage baggage;
    public Usb usb;
    public Entertainment entertainment;
    public SeatPitch seatPitch;
    public SeatTilt seatTilt;
    public SeatWidth seatWidth;
}

public class GiftVoucher{
    public Object currency;
    public int discountAmount;
    public double discountPercentage;
    public Object discountType;
    public Object adjustmentCode;
    public int maximumVoucherAmount;
    public int minimumTransactionAmount;
    public Object purpose;
    public Object gvKey;
    public Object validFromDate;
    public Object validUntilDate;
    public Object emailTemplateKey;
    public Object validityRestrictions;
    public Object voucherType;
}

public class HeaderText{
    @JsonProperty("ID") 
    public String iD;
    @JsonProperty("EN") 
    public String eN;
}

public class InclusiveInformation{
    public ArrayList<DepartureInclusive> departureInclusive;
    public ArrayList<Object> returnInclusive;
}

public class InfantFare{
    public int total;
    public int totalWithoutAdjustment;
    public int totalWithMarkupWithoutDiscount;
    public int base;
    public int commission;
    public int iwjr;
    public int tax;
    public int markup;
    public int discount;
    public Object discountDetail;
    public int seatAvailables;
    public int supplierDiscount;
    public int loyaltyPoint;
    public int insurance;
    public Object premiNumber;
    public int totalWithInsurance;
    public ArrayList<Object> penaltyInfos;
    public Object fareBasisCodes;
    public Object refundInformation;
    public Object adjustmentId;
    public Object campaignId;
    public int d5;
}

public class Info{
    public String icon;
    public String title;
    public ArrayList<String> descriptions;
}

public class InputSource{
    public String name;
    public Object label;
    public String value;
    public Object image;
    public Object imageDetail;
    public Object measurement;
    public Object currency;
    public int price;
    public int tixPoint;
    public Object description;
}

public class Insurance{
    public int pricePerPax;
    public int compensationAmount;
    public int priceTotal;
    public Object currency;
    public Object content;
    public Object url;
}

public class IssuancePeriods{
    public String startDate;
    public String endDate;
}

public class ItemDetail{
    public String journeyType;
    public String flightId;
    public String flightSelect;
    public String currency;
    public String origin;
    public String destination;
    public Object date;
    public MarketingAirline marketingAirline;
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
    public ArrayList<Schedule> schedules;
    public ArrayList<String> facilitiesPriority;
    public boolean holdable;
    public boolean upgradable;
    public boolean refundable;
    public boolean international;
    public Object fareSellKey;
    public Object journeySellKey;
    public Object marriageGroup;
    public boolean roundTrip;
    public String cabinClass;
    public ArrayList<String> tags;
    public ArrayList<String> facilityTags;
    public Date cacheTimestamp;
    public int fareRankAll;
    public int fareRankAirline;
    public int departRankEarliestAll;
    public int departRankEarliestAirline;
    public int departRankLatestAll;
    public int departRankLatestAirline;
    public CacheDetail cacheDetail;
    public boolean crossFare;
}

public class MarketingAirline{
    public String code;
    public String name;
    public String shortName;
    public String displayName;
    public String urlIcon;
}

public class Meal{
    public String icon;
    public boolean value;
    public String name;
    public String url;
    public String description;
}

public class MealPax{
    public Object url;
    public ArrayList<Object> adultMeal;
    public ArrayList<Object> childMeal;
}

public class MultiCurrency{
    public String currency;
    public int scale;
}

public class MultiInsurance{
    public int pricePerPax;
    public int pricePaxChild;
    public int pricePaxAdult;
    public int pricePaxInfant;
    public int totalPax;
    public int compensationAmount;
    public int priceTotal;
    public String currency;
    public Object content;
    public String url;
    public String name;
    public DescriptionContent descriptionContent;
    public String descriptionIcon;
    public Object confirmation;
    public String tnc;
    public String howToClaim;
    public String code;
    public ArrayList<Coverage> coverages;
    public PriceDetail priceDetail;
    public ArrayList<CredentialRule> credentialRule;
    public ArrayList<Object> additionalContents;
    public String template;
    public String referenceDetail;
    public String credentialCode;
}

public class Nobaggage{
    public String name;
    public String url;
    public String description;
}

public class NudgesText{
    public String lang;
    public String text;
}

public class OperatingAirline{
    public String code;
    public String name;
    public String shortName;
    public String displayName;
    public String urlIcon;
}

public class OrderItemAttribute{
    public String key;
    public String value;
}

public class OrderNudge{
    public ArrayList<NudgesText> nudgesText;
}

public class OriginalAdultFare{
    public int total;
    public int totalWithoutAdjustment;
    public int totalWithMarkupWithoutDiscount;
    public int base;
    public int commission;
    public int iwjr;
    public int tax;
    public int markup;
    public int discount;
    public Object discountDetail;
    public int seatAvailables;
    public int supplierDiscount;
    public int loyaltyPoint;
    public int insurance;
    public Object premiNumber;
    public int totalWithInsurance;
    public ArrayList<Object> penaltyInfos;
    public Object fareBasisCodes;
    public Object refundInformation;
    public Object adjustmentId;
    public Object campaignId;
    public int d5;
}

public class OriginalChildFare{
    public int total;
    public int totalWithoutAdjustment;
    public int totalWithMarkupWithoutDiscount;
    public int base;
    public int commission;
    public int iwjr;
    public int tax;
    public int markup;
    public int discount;
    public Object discountDetail;
    public int seatAvailables;
    public int supplierDiscount;
    public int loyaltyPoint;
    public int insurance;
    public Object premiNumber;
    public int totalWithInsurance;
    public ArrayList<Object> penaltyInfos;
    public Object fareBasisCodes;
    public Object refundInformation;
    public Object adjustmentId;
    public Object campaignId;
    public int d5;
}

public class OriginalInfantFare{
    public int total;
    public int totalWithoutAdjustment;
    public int totalWithMarkupWithoutDiscount;
    public int base;
    public int commission;
    public int iwjr;
    public int tax;
    public int markup;
    public int discount;
    public Object discountDetail;
    public int seatAvailables;
    public int supplierDiscount;
    public int loyaltyPoint;
    public int insurance;
    public Object premiNumber;
    public int totalWithInsurance;
    public ArrayList<Object> penaltyInfos;
    public Object fareBasisCodes;
    public Object refundInformation;
    public Object adjustmentId;
    public Object campaignId;
    public int d5;
}

public class OriginalTotalFare{
    public int total;
    public int totalWithoutAdjustment;
    public int totalWithMarkupWithoutDiscount;
    public int base;
    public int commission;
    public int iwjr;
    public int tax;
    public int markup;
    public int discount;
    public Object discountDetail;
    public int seatAvailables;
    public int supplierDiscount;
    public int loyaltyPoint;
    public int insurance;
    public Object premiNumber;
    public int totalWithInsurance;
    public ArrayList<Object> penaltyInfos;
    public Object fareBasisCodes;
    public Object refundInformation;
    public Object adjustmentId;
    public Object campaignId;
    public int d5;
}

public class PassengerForm{
    public int countAdult;
    public int countChild;
    public int countInfant;
    public Object adultForm;
    public Object childForm;
    public Object infantForm;
}

public class PassengerFormSection{
    public int countAdult;
    public int countChild;
    public int countInfant;
    public ArrayList<AdultFormSection> adultFormSections;
    public ArrayList<Object> childFormSections;
    public ArrayList<Object> infantFormSections;
}

public class PaymentFlexibleNudge{
    public String lang;
    public String text;
}

public class PaymentNudges{
    public String refund;
    public String reschedule;
    public ArrayList<PaymentFlexibleNudge> paymentFlexibleNudge;
}

public class PenaltyFeeDescriptions{
    public String reschedule;
    public String refund;
}

public class PenaltyInfo{
    public String penaltyType;
    public String penaltyStatus;
    public int minutes;
    public String timeFrame;
    public int amount;
    public String currencyCode;
    public int decimalPlaces;
    public int originalAmount;
    public String originalCurrencyCode;
    public int originalDecimalPlaces;
    public String penaltyDescription;
}

public class Policy{
    public RefundPolicy refundPolicy;
    public ReschedulePolicy reschedulePolicy;
    public ArrayList<String> departureReschedulePolicy;
    public ArrayList<Object> returnReschedulePolicy;
    public ArrayList<String> departureRefundPolicy;
    public ArrayList<Object> returnRefundPolicy;
}

public class PriceDetail{
    public double adult;
    public double child;
    public double infant;
}

public class Punctuality{
    public Object icon;
    public Object value;
}

public class Refund{
    public String desc;
    public int amountAdult;
    public Object amountChild;
    public Object amountInfant;
    public String title;
    public String subtitle;
    public EventTermAndCondition eventTermAndCondition;
}

public class RefundPolicy{
    @JsonProperty("TIKETCOM_JT") 
    public TIKETCOMJT tIKETCOM_JT;
}

public class RefundProperties{
    @JsonProperty("YES") 
    public YES yES;
}

public class REFUNDWITHOUTINSURANCE{
    public String header;
    public ArrayList<String> termAndConditions;
}

public class REFUNDWITHOUTINSURANCEINFOV4{
    public String header;
    public ArrayList<String> termAndConditions;
}

public class REFUNDWITHOUTINSURANCEV4{
    public String header;
    public ArrayList<String> termAndConditions;
}

public class RefundYes{
    public String name;
    public String url;
    public String description;
}

public class Reschedule{
    public String desc;
    public int amountAdult;
    public Object amountChild;
    public Object amountInfant;
    public String title;
    public String subtitle;
    public EventTermAndCondition eventTermAndCondition;
}

public class ReschedulePolicy{
    @JsonProperty("TIKETCOM_JT") 
    public TIKETCOMJT tIKETCOM_JT;
}

public class RescheduleProperties{
    @JsonProperty("YES") 
    public YES yES;
}

public class RESCHEDULEWITHOUTINSURANCE{
    public String header;
    public ArrayList<String> termAndConditions;
}

public class RESCHEDULEWITHOUTINSURANCEINFOV4{
    public String header;
    public ArrayList<String> termAndConditions;
}

public class RESCHEDULEWITHOUTINSURANCEV4{
    public String header;
    public ArrayList<String> termAndConditions;
}

public class RescheduleYes{
    public String name;
    public String url;
    public String description;
}

public class Root{
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public Date serverTime;
}

public class Schedule{
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
    public TransitTime transitTime;
    public Baggage baggage;
    public ArrayList<BaggagePerPax> baggagePerPax;
    public ArrayList<Object> connectings;
    public Facilities facilities;
    public FacilitiesValue facilitiesValue;
    public boolean facilitiesReady;
    public Object marriageGroup;
    public OperatingAirline operatingAirline;
    public String operatingFlightNumber;
    public boolean bundlingMeal;
    public boolean bundlingBaggage;
    public ArrayList<String> tags;
    public ArrayList<String> facilityTags;
    public Object benefits;
    public Info info;
}

public class SeatLayout{
    public Object icon;
    public Object value;
}

public class SeatPax{
    public Object url;
    public int retryLimitAddons;
}

public class SeatPitch{
    public String name;
    public String url;
    public String description;
}

public class SeatTilt{
    public String name;
    public String url;
    public String description;
}

public class SeatWidth{
    public String name;
    public String url;
    public String description;
}

public class ShortDescriptions{
    @JsonProperty("ID") 
    public String iD;
    @JsonProperty("EN") 
    public String eN;
}

public class TagProperty{
    public String type;
    public String name;
    public String headerText;
    public String headerDescription;
    public String headerIcon;
    public String listIcon;
    public String listText;
    public Object linkText;
    public Object link;
    public String category;
    public String bundlingType;
    public int priority;
}

public class TIKETCOMJT{
    public String content;
    public String icon;
    public String title;
}

public class TotalFare{
    public int total;
    public int totalWithoutAdjustment;
    public int totalWithMarkupWithoutDiscount;
    public int base;
    public int commission;
    public int iwjr;
    public int tax;
    public int markup;
    public int discount;
    public DiscountDetail discountDetail;
    public int seatAvailables;
    public int supplierDiscount;
    public int loyaltyPoint;
    public int insurance;
    public Object premiNumber;
    public int totalWithInsurance;
    public ArrayList<Object> penaltyInfos;
    public Object fareBasisCodes;
    public Object refundInformation;
    public Object adjustmentId;
    public Object campaignId;
    public int d5;
}

public class TransitTime{
    public int day;
    public int hour;
    public int minute;
}

public class TravelTime{
    public int day;
    public int hour;
    public int minute;
}

public class Usb{
    public String icon;
    public boolean value;
    public String name;
    public String url;
    public String description;
}

public class Validator{
    public String name;
    public String message;
    public String parameter;
}

public class Wifi{
    public String icon;
    public boolean value;
    public String name;
    public String url;
    public String description;
}

public class YES{
    public String title;
    public String icon;
}


}
