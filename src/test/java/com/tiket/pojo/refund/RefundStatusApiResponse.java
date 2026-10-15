package com.tiket.pojo.refund;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.util.ArrayList;
import java.util.Date;

public class RefundStatusApiResponse {

public Data data;

/**
 * Some monetary/amount fields (e.g. {@code vendorIncentive}) are declared as {@code int} but the API can
 * return decimals (such as {@code 97355.987}), which makes strict Gson parsing fail. This deserializer
 * tolerates decimals by truncating them, so parsing never breaks on an unexpected fractional number.
 */
private static final JsonDeserializer<Integer> LENIENT_INT = (json, type, context) -> {
    if (json == null || json.isJsonNull() || !json.isJsonPrimitive()) {
        return 0;
    }
    try {
        String raw = json.getAsString().trim();
        if (raw.isEmpty()) {
            return 0;
        }
        return (int) Double.parseDouble(raw);
    } catch (RuntimeException e) {
        return 0;
    }
};

private static final Gson LENIENT_GSON = new GsonBuilder()
        .registerTypeAdapter(int.class, LENIENT_INT)
        .registerTypeAdapter(Integer.class, LENIENT_INT)
        .create();

public static RefundStatusApiResponse parse(String json) {
    return LENIENT_GSON.fromJson(json, RefundStatusApiResponse.class);
}

public class AccountDetails{
    public String accountName;
    public String accountNumber;
}

public class AdditionalDetails{
    public int accountId;
    public Object addOns;
    public AdditionalInfo additionalInfo;
    public String addressCountry;
    public String areaName;
    public int bookingAdult;
    public String bookingCheckinDate;
    public String bookingCheckoutDate;
    public int bookingChild;
    public int bookingNight;
    public int bookingRoom;
    public boolean breakfast;
    public Object cancellationDetails;
    public ArrayList<CancellationPolicyV2> cancellationPolicyV2;
    public CancellationPolicyV3 cancellationPolicyV3;
    public String checkinGuideUrl;
    public String cityName;
    public String countryName;
    public String departureAt;
    public EmailInfo emailInfo;
    public Object flexInfoCards;
    public String holdPaymentStatus;
    public String hotelAddress;
    public String hotelCartId;
    public String hotelChain;
    public String hotelCheckinInstruction;
    public String hotelCityId;
    public String hotelCountryId;
    public String hotelId;
    public String hotelImage;
    public String hotelItineraryNumber;
    public double hotelLatitude;
    public double hotelLongitude;
    public String hotelPhone;
    public String hotelPolicy;
    public String hotelProvinceId;
    public String hotelSpecialCheckinInstruction;
    public ArrayList<Info> info;
    public String issuedTimestamp;
    public String itineraryId;
    public MealPlan mealPlan;
    public String nativeId;
    public boolean nonRebookable;
    public String orderDate;
    public int orderDetailId;
    public int orderId;
    public String orderName;
    public String orderNameDetail;
    public String partnerName;
    public boolean payAtHotel;
    public String paymentType;
    public String pgName;
    public String provinceName;
    public int rebookedFrom;
    public String rebookedHashFrom;
    public String rebookedHashTo;
    public int rebookedTo;
    public String rebookingData;
    public int rescheduledFrom;
    public String rescheduledHashFrom;
    public String rescheduledHashTo;
    public int rescheduledTo;
    public String roomBedType;
    public String roomCancellationPolicy;
    public int roomMaxOccupancy;
    public String roomName;
    public String roomRateInfo;
    public String roomSmoking;
    public String roomSource;
    public String seeDetails;
    public Object specialRequest;
    public int starRating;
    public String status;
    public String supplierItineraryId;
    public String surcharges;
    public int taxAndOtherFee;
    public int tixhotelId;
    public int totalAmountOrder;
    public int totalAmountOrderDetail;
    public int totalPayment;
    public int totalPromo;
    public int totalWithoutTax;
    public ArrayList<Traveler> travelers;
    public ArrayList<Traveller> travellers;
    public int vendorIncentive;
    public boolean wifi;
}

public class AdditionalIds{
    public String itineraryId;
}

public class AdditionalInfo{
    public String acceptLanguage;
    public String businessId;
    public String channelId;
    public String requester;
    public String resellerId;
    public String serviceId;
    public String source;
    public String storeId;
    public String username;
}

public class AdditionalReferenceIds{
    public String bookingCode;
    public String itineraryId;
    public String rrId;
    public String splitCode;
}

public class AdditionalTerms{
    public ArrayList<String> en;
    public ArrayList<String> id;
}

public class AmountDetail{
    public int id;
    public int refundRequestId;
    public String amountName;
    public String amountLabel;
    public String amountType;
    public int amount;
    public String amountFormatted;
    public String currency;
    public String notes;
    public int amount_from_vendor;
    public int extra_fee;
    public int unique_code;
}

public class Body{
    public String en;
    public String id;
}

public class Breakdown{
    public int insurance;
    public int insuranceCoverage;
    public boolean isExcludeBankFee;
    public boolean isExcludeRefundFee;
    public Object items;
    public int percentageRefundFee;
    public int refundFee;
    public String refundFeeFormatted;
    public int totalAmountFromVendor;
    public int totalPaid;
    public String totalPaidFormatted;
    public int vendorPrice;
    public String vendorPriceFormatted;
}

public class CancellationPolicyV2{
    public int amount;
    public Date time;
}

public class CancellationPolicyV3{
    public AdditionalTerms additional_terms;
    public Body body;
    public boolean free_cancellation;
    public ArrayList<Policy> policies;
    public boolean refundable;
}

public class Characteristics{
    public boolean hasAdminFee;
    public boolean isCalculatePromo;
    public boolean isDeductRefundLimit;
    public boolean isIncludePaymentPartnerFee;
    public boolean isOverPayment;
    public boolean isReflectToOrder;
}

public class Charged{
    public String currency_code;
    public int units;
}

public class Contact{
    public int accountId;
    public String name;
    public String email;
    public String phone;
    public Object details;
    public String salutation;
}

public class Data{
    public ArrayList<Refund> refunds;
    public int count;
    public int page;
    public int size;
    public int totalPage;
    public boolean hasNext;
}

public class Details{
    public int accountId;
    public Object addOns;
    public AdditionalInfo additionalInfo;
    public String addressCountry;
    public String areaName;
    public int bookingAdult;
    public String bookingCheckinDate;
    public String bookingCheckoutDate;
    public int bookingChild;
    public int bookingNight;
    public int bookingRoom;
    public boolean breakfast;
    public Object cancellationDetails;
    public ArrayList<CancellationPolicyV2> cancellationPolicyV2;
    public CancellationPolicyV3 cancellationPolicyV3;
    public String checkinGuideUrl;
    public String cityName;
    public String countryName;
    public String departureAt;
    public EmailInfo emailInfo;
    public Object flexInfoCards;
    public String holdPaymentStatus;
    public String hotelAddress;
    public String hotelCartId;
    public String hotelChain;
    public String hotelCheckinInstruction;
    public String hotelCityId;
    public String hotelCountryId;
    public String hotelId;
    public String hotelImage;
    public String hotelItineraryNumber;
    public double hotelLatitude;
    public double hotelLongitude;
    public String hotelPhone;
    public String hotelPolicy;
    public String hotelProvinceId;
    public String hotelSpecialCheckinInstruction;
    public ArrayList<Info> info;
    public String issuedTimestamp;
    public String itineraryId;
    public MealPlan mealPlan;
    public String nativeId;
    public boolean nonRebookable;
    public String orderDate;
    public int orderDetailId;
    public int orderId;
    public String orderName;
    public String orderNameDetail;
    public String partnerName;
    public boolean payAtHotel;
    public String paymentType;
    public String pgName;
    public PriceMatrix priceMatrix;
    public String provinceName;
    public int rebookedFrom;
    public String rebookedHashFrom;
    public String rebookedHashTo;
    public int rebookedTo;
    public String rebookingData;
    public int rescheduledFrom;
    public String rescheduledHashFrom;
    public String rescheduledHashTo;
    public int rescheduledTo;
    public String roomBedType;
    public String roomCancellationPolicy;
    public int roomMaxOccupancy;
    public String roomName;
    public String roomRateInfo;
    public String roomSmoking;
    public String roomSource;
    public String seeDetails;
    public Object specialRequest;
    public int starRating;
    public String status;
    public String supplierItineraryId;
    public String surcharges;
    public int taxAndOtherFee;
    public int tixhotelId;
    public int totalAmountOrder;
    public int totalAmountOrderDetail;
    public int totalPayment;
    public int totalPromo;
    public int totalWithoutTax;
    public ArrayList<Traveler> travelers;
    public ArrayList<Traveller> travellers;
    public int vendorIncentive;
    public boolean wifi;
}

public class EmailInfo{
    public String deeplinkExpiredAt;
    public Date endAt;
    public Date endAt2;
    public EstimatedTotalPayment estimatedTotalPayment;
    public ArrayList<Item> items;
    public int orderId;
    public Object refundPaymentMethod;
    public String requestDate;
    public String requestHash;
    public String requestTime;
    public String requesterName;
    public Date startAt;
    public Date startAt2;
    public String vertical;
}

public class EstimatedTotalPayment{
    public String mainCurrency;
    public String tixPoint;
}

public class GroupedPaymentMethod{
    public int id;
    public int refundRequestId;
    public String methodKey;
    public String methodKeyName;
    public boolean isSavedDetails;
    public int amount;
    public String amountFormatted;
    public String status;
    public String currency;
    public AccountDetails accountDetails;
    public MerchantDetails merchantDetails;
    public String notes;
    public String updatedBy;
    public Date updatedAt;
    // public PaymentIds paymentIds;
    public boolean isPrimary;
    public int orderId;
    public int orderDetailId;
    public int priority;
    public boolean isConvertedToBlipay;
    public String disbursementLog;
    public String refundedMessage;
}

public class Guests{
    public int adult;
    public int child;
    public int total;
}

public class Info{
    public String checkinDate;
    public String checkoutDate;
    public String country;
    public int duration;
    public Guests guests;
    public String hotelImage;
    public Object hotelRequest;
    public String orderedHotel;
    public String orderedRoom;
    public String province;
    public int quantity;
    public Object roomFacilities;
}

public class Item{
    public ArrayList<String> descriptions;
    public String title;
}

public class MealPlan{
    public int breakfastPax;
    public MealPlanV2 mealPlanV2;
}

public class MealPlanV2{
    public String en;
    public String id;
}

public class MerchantDetails{
    public String merchantKey;
    public String merchantName;
    public String pgName;
}

public class Order{
    public int orderId;
    public int orderDetailId;
    public int newOrderId;
    public int newOrderDetailId;
    public String orderHash;
    public Details details;
    public Snapshot snapshot;
    public Date departureAt;
    public EmailInfo emailInfo;
    public Object travelers;
    public String vertical;
    public String currency;
    public boolean isB2BCorporate;
    public String detailDesc;
    public int price;
    public String promoCode;
    public int totalPromoCode;
    public String giftVoucher;
    public int totalGiftVoucher;
    public int totalTixPoint;
    public int subsidy;
    public int totalMarkup;
    public String dot;
    public String customerType;
}

public class PaymentManifest{
    public String accountName;
    public String accountNumber;
    public int amount;
    public String amountFormatted;
    public int cardExpiryMonth;
    public int cardExpiryYear;
    public int differenceBetweenPurchaseAndRefundDate;
    public int extraFee;
    public String merchant;
    public String method;
    public Date paymentAt;
    public String paymentId;
    public String paymentReferenceId;
    public String pgName;
    public int referenceOrderID;
    public String tenor;
    public int uniqueCode;
    public String extraFeeFormatted;
    public String uniqueCodeFormatted;
    public boolean isConvertedToBlipay;
    public int priority;
}

public class PaymentManifestReport{
    public String ccApprovalCode;
    public String paymentMethod;
}

public class PaymentMethod{
    public int id;
    public int refundRequestId;
    public String methodKey;
    public String methodKeyName;
    public boolean isSavedDetails;
    public int amount;
    public String amountFormatted;
    public String status;
    public String currency;
    public AccountDetails accountDetails;
    public MerchantDetails merchantDetails;
    public String notes;
    public String updatedBy;
    public Date updatedAt;
    // public PaymentIds paymentIds;
    public boolean isPrimary;
    public int orderId;
    public int orderDetailId;
    public int priority;
    public boolean isConvertedToBlipay;
    public String disbursementLog;
    public String refundedMessage;
}

public class PaymentMethodReport{
    public String bankName;
    public String bankAccountNumber;
    public String bankAccountName;
    public String refundMethod;
    public String pgName;
    public String customerCardType;
    public String ccInstallment;
}

public class Policy{
    public Charged charged;
    public Date deadline;
    public boolean is_no_show_policy;
    public double percentage;
}

public class PriceMatrix{
    public Breakdown breakdown;
    public String estimationLogId;
    public String estimationMessage;
    public String externalReferenceId;
    public String generalInfo;
    public String orderDetailId;
    public String orderHash;
    public String orderId;
    public PriceMatrix priceMatrix;
    public ArrayList<RefundAmount> refundAmounts;
    public ArrayList<RefundFee> refundFees;
    public String refundMethod;
    public String status;
    public int timeLimitMillis;
    public String currency;
    public Date endDate;
    public ArrayList<Object> generalFee;
    public ArrayList<Object> itemPrice;
    public int refundAmount;
    public String refundAmountFormatted;
    public Object startDate;
}

public class Reason{
    public String id;
    public String name;
    public String type;
    public Characteristics characteristics;
    public String chooseItem;
}

public class Refund{
    public int id;
    public Contact contact;
    public Order order;
    public String requestHash;
    public AdditionalInfo additionalInfo;
    public AdditionalReferenceIds additionalReferenceIds;
    public Reason reason;
    public String requestRemark;
    public String refundOptionKey;
    public Date requestedAt;
    public Object deeplinkExpiredAt;
    public Date paymentMethodSubmittedAt;
    public Object items;
    public ArrayList<Status> status;
    public ArrayList<PaymentMethod> paymentMethods;
    public ArrayList<GroupedPaymentMethod> groupedPaymentMethods;
    public ArrayList<AmountDetail> amountDetails;
    public String currency;
    public String vertical;
    public String requestType;
    public String resellerType;
    public int actualRefundAmount;
    public String actualRefundAmountFormatted;
    public String actualRefundAmountSource;
    public ArrayList<PaymentManifest> paymentManifest;
    public Date createdAt;
    public Date updatedAt;
    public String createdBy;
    public String updatedBy;
    public String lastStatus;
    public String lastStatusLabel;
    public String rejectedReason;
    public String rejectedLabel;
    public int totalAmount;
    public String totalAmountFormatted;
    public int totalAmountFromVendor;
    public String totalAmountFromVendorFormatted;
    public int totalRefundNonTixPointAmount;
    public String totalRefundNonTixPointAmountFormatted;
    public String chooseItem;
    public boolean isAutomated;
    public String automationCommand;
    public String lastShownStatus;
    public Object accountDeactivatedAt;
    public Object refundDocuments;
    public String campaignProductName;
    public String policyNumber;
    public String policyNumberFormatted;
    public int billedToInsurer;
    public String billedToInsurerFormatted;
    public boolean isReschedule;
    public String baseCurrency;
    public String fxRate;
    public String entity;
    public boolean manualChecking;
    // @JsonProperty("OptionalInfo") 
    public Object optionalInfo;
    public int estimateAmountFromVendor;
    public String partnerStatus;
    public String validateInfo;
    public boolean isB2BCorporate;
    public int penaltyFee;
    public int refundFee;
    public int adminFee;
    public String requester;
    public boolean isPartialRefund;
    public String createdAtFormatted;
    public String cancellationMessage;
    public String marginTime;
    public String memberChannel;
    public String dueDate;
    public Date financeReceiveDate;
    public int countDate;
    public String logDate;
    public String actualRequestDate;
    public VerticalInfo verticalInfo;
    public PaymentMethodReport paymentMethodReport;
    public PaymentManifestReport paymentManifestReport;
    public boolean isExcludeBankFee;
    public String claimInsuranceStatus;
    public RuleValidation ruleValidation;
    public String customerType;
    public Object refundEstimationDate;
}

public class RefundAmount{
    public int amount;
    public String amountFormatted;
    public String key;
    public String label;
}

public class RefundFee{
    public int amount;
    public String amountFormatted;
    public String key;
    public String label;
}

public class Root{
    public String code;
    public Data data;
    public int serverTime;
    public String requestId;
}

public class RuleValidation{
    public int ruleId;
    public String ruleName;
    public Object rule;
}

public class Snapshot{
    public AdditionalDetails additionalDetails;
    public AdditionalIds additionalIds;
    // public AmountDetails amountDetails;
    public Object amountDetailsFormatted;
    public String baseCurrency;
    public String campaignProductName;
    public Contact contact;
    public String currency;
    public ArrayList<Object> discounts;
    public String entity;
    public String fxRate;
    public int id;
    public boolean isB2BCorporate;
    public ArrayList<Object> items;
    public int orderDetailId;
    public String orderHash;
    public String orderStatus;
    public ArrayList<PaymentManifest> paymentManifest;
    public Object relatedOrder;
    public String resellerId;
    public String resellerType;
    public int sellingPrice;
    public int totalPayment;
    public String totalPaymentFormatted;
    public String vertical;
}

public class Status{
    public String status;
    public String statusLabel;
    public String description;
    public String imageDescription;
    public String colorDescription;
    public int sequence;
    public Date finishedAt;
    public Object estimationAt;
    public boolean isShowUser;
    public String createdBy;
    public String actorType;
}

public class Traveler{
    public String email;
    public String name;
    public int orderDetailId;
    public String phone;
    public String salutation;
    public String type;
}

public class Traveller{
    public String accountPhone;
    public String addressCountry;
    public String firstName;
    public String fullName;
    public int guestNumber;
    public String lastName;
    public int profileId;
    public String salutation;
    public String specialRequest;
}

public class VerticalInfo{
    public String vendorId;
    public String vendorName;
    public String subVendorName;
    public int adultTicketPrice;
    public int childTicketPrice;
    public int infantTicketPrice;
    public int adultCount;
    public int childCount;
    public int infantCount;
    public String category;
}


}
