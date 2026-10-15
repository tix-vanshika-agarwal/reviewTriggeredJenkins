package com.tiket.pojo.intools;

import java.util.List;

public class SummaryReportDetail {

    public List<SummaryDetail> data;

    public class SummaryDetail {

        public String vendorName;
        public String currency;
        public String totalQuantitySold;
        public Double revenueFromOrders;
        public Double additionalMargin;
        public Double additionalCommission;
        public Double totalVAT;
        public Double totalWHT;
        public Double topUpAmountFromVendor;
        public Double totalCommissionEarned;
        public Double totalVendorShare;
        public Double directCosts;
        public Double profit;
        public String settlementType;
        public Double currentDepositBalance;
        public Double startDepositBalance;
        public Double endDepositBalance;

    }


}