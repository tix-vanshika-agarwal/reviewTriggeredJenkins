package com.tiket.pojo.intools;

import java.util.List;

public class B2BResellerDetail {
    public List<Data> data;


    public class Data {
        public int id;
        public String resellerId;
        public String resellerName;
        public String entityId;
        public String status;
        public String settlementType;
        public String contractType;
        public String markup;
        public String commission;
        public String commissionType;
        public long depositBalance;
        public int depositThreshold;
        public int depositBuffer;
        public String startDate;
        public String endDate;
        public List<String> b2bVertical;
        public List<String> categories;
        public String preferredCurrency;
        public int vat;
        public int withholdingTax;
        public boolean extensible;
        public boolean allowNegativeDeposit;
        public boolean depositApplicable;
        public boolean isDepositBalanceUpdated;
        public boolean creditLineApplicable;
        public boolean invoicingApplicable;
        public String invoicingStartFrom;
        public String invoicingFrequency;
        public String mainSettlementType;
        public boolean autoSettlement;

        public CreditLine creditLine;
    }

    public class CreditLine {
        public int resellerId;
    }
}