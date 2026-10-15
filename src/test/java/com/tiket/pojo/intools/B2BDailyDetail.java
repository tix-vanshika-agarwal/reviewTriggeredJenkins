package com.tiket.pojo.intools;

import java.util.List;

public class B2BDailyDetail {
    public List<Data> data;


    public class Data {
        public String id;
        public String uid;
        public int configId;
        public String orderType;
        public String vertical;
        public String resellerName;
        public String resellerId;
        public AgentInfo agentInfo;
        public List<Detail> details;
        public String resellerCustomerEmail;
        public String agreementType;
        public String profitType;
        public String orderId;
        public String resellerOrderId;
        public String orderStatus;
        public String paymentStatus;
        public String paymentDate;
        public String campaignName;
        public String customerCurrency;
        public int customerFinalPriceInCustomerCurrency;
        public int customerFinalPriceInVendorPreferredCurrency;
        public String resellerPreferredCurrency;
        public String paymentStatusFromDisbursement;
        public String settlementDate;
        public int profit;
        public int totalTaxOnOrderLevel;
        public int netMargin;
        public int depositDeductionAmount;
        public String proofOfTransfer;
        public int topUpAmount;
        public int transactionAmount;
        public String typeOfAdjustment;
        public String typeOfTransaction;
        public String description;
        public String createdAt;
        public String entity;
        public String source;
        public String userEmail;
        public boolean isProcessed;
    }

    public class AgentInfo {
        public String id;
        public String name;
        public String subVendorName;
    }

    public class Detail {
        public String productName;
        public String itemDetail;
        public String bookingCode;
        public long orderDetailID;
        public int quantity;
        public int sellingPrice;
        public int basicPrice;
        public int markUpPercentage;
        public int markUpValue;
        public int markUpValueFromMarkUpPercentage;
        public int totalMarkUpValue;
        public int resellerCommissionPercentage;
        public int airlineCommission;
        public int commissionValue;
        public int tiketCommissionValue;
        public int publishedFareToReseller;
        public int vat;
        public int withholding;
        public int resellerSubsidyCampaignPercentage;
        public int resellerSubsidyCampaignValue;
        public int vatOnTotalMarkUpAndCommission;
        public int netPartnerCommissionAmount;
        public int netMarkUpAndCommission;
        public String routeDisplay;
        public int withholdingTaxValue;
        public String validityDate;
        public String validityDateTimezone;
        public String orderName;
        public List<String> fileName;
        public List<String> fileUrl;
        public String category;
        public int giftVoucher;
        public int promocodeValue;
        public int tiketPoint;
        public int tiketCampaignSubsidy;
        public int customerFinalPriceInVPC;
        public int settlementAmountInRPC;
        public String campaignName;
        public String settlementDate;
        public int baseFare;
        public int currentArrears;
        public int previousArrears;
        public double startDepositBalance;
        public double endDepositBalance;
        public double startCreditLineBalance;
        public double endCreditLineBalance;
        public double creditLineUtil;
        public int creditLineAmount;
        public int invoicingAmount;
        public int convenienceFee;
        public int publishedFareBaggage;
        public int publishedFareMeals;
        public int publishedFareSeat;
        public int publishedFareTicket;
        public int basicPriceBaggage;
        public int basicPriceMeals;
        public int basicPriceSeat;
        public int basicPriceTicket;
        public int totalMarkUpOnBaggage;
        public int totalMarkUpOnMeals;
        public int totalMarkUpOnSeat;
        public int totalMarkUpOnTicket;
        public String fscLcc;
        public String supplier;
        public String chainName;
        public String preferredPartnerType;

    }
}