package com.tiket.pojo.intools;

import java.util.List;

public class B2CDailyDetailOrder {

    public List<Data> data;

    public static class Data {

        public String orderId;
        public Double totalTax;
        public Double customerFinalPriceInVendorCurrency;
        public Double customerFinalPrice;
        public String customerCurrency;

        public Double profit;
        public Double netMargin;
        public Double convenienceFee;

        public List<Detail> detail;

    }

    public static class Detail {

        public String vendorId;

        public String orderDetailId;

        public String source;
        public String settlementType;
        public Double baseFare;
        public Double basicPrice;
        public Double basicPriceInCustomerCurrency;
        public Double basicPriceInReportCurrency;
        public Double otherFees;
        public Double basicPriceTicketInCustomerCurrency;
        public Double basicPriceBaggageInCustomerCurrency;
        public Double basicPriceMealsInCustomerCurrency;
        public Double basicPriceSeatInCustomerCurrency;
        public Double basicPriceTicketInReportCurrency;
        public Double basicPriceBaggageInReportCurrency;
        public Double basicPriceMealsInReportCurrency;
        public Double basicPriceSeatInReportCurrency;
        public Double markupTotal;
        public Double totalMarkUpOnTicketInCustomerCurrency;
        public Double totalMarkUpOnBaggageInCustomerCurrency;
        public Double totalMarkUpOnMealsInCustomerCurrency;
        public Double totalMarkUpOnSeatInCustomerCurrency;
        public Double commissionPercentage;
        public Double commissionPercentageValue;
        public Double commissionTotal;
        public Double commissionValueInCustomerCurrency;
        public Double netMarkup;
        public Double publishedFare;
        public Double ticketPriceInCustomerCurrency;
        public Double baggagePriceInCustomerCurrency;
        public Double mealPriceInCustomerCurrency;
        public Double seatPriceInCustomerCurrency;
        public Double baseFareInReportCurrency;
        public String vendorCurrency;
        public Double settlementAmountInVendorCurrency;
        public Double settlementAmountInCustomerCurrency;
        public Double settlementAmountInReportCurrency;
        public Double vat;
        public Double withholdingTax;
        public Double vatValue;
        public Double withholdingTaxValue;
        public Double startDepositBalance;
        public Double endDepositBalance;
        public Double markupValue;
        public Double totalTiketRefundRatesMarkUp;
        public Double totalPartnerRefundRatesMarkUp;
        public Double lowestPrice;
        public Double lowestPriceSubsidy;
        public Double actualPublishedFare;
        public Double totalAmountDiff;
        public Double vendorSubsidyCampaignValue;
        public Double vendorSubsidyLoyaltyValue;
        public Double vendorSubsidyBuyXGetY;
        public Double partnerSubsidyBundlingValue;
        public Double tiketSubsidyCampaignPercentage;
        public Double tiketSubsidyLoyaltyPercentage;
        public Double tiketSubsidyCampaignValue;
        public Double tiketSubsidyLoyaltyValue;
        public Double tiketSubsidyBuyXGetY;
        public Double tiketSubsidyBundlingValue;
        public Double vendorSubsidyCampaignPercentage;
        public Double vendorSubsidyLoyaltyPercentage;
        public Double dynamicPricingRevenue;
        public Double dynamicPricingCost;
        public Double markupPercentageValue;
        public Double deductionDeposit;

        public List<Items> items;

        public static class Items {
            public Double markupValue;
            public Double basicPrice;
            public Double markupPercentageValue;

        }
    }
}