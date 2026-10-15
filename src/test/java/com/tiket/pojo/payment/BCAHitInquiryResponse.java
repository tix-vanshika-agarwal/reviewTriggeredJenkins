package com.tiket.pojo.payment;

import java.util.List;

public class BCAHitInquiryResponse {
    public String CurrencyCode;
    public List<FreeText> FreeTexts;
    public String AdditionalData;
    public InquiryReason InquiryReason;
    public String RequestID;
    public String SubCompany;
    public String CustomerNumber;
    public String InquiryStatus;
    public String CustomerName;
    public String TotalAmount;
    public String CompanyCode;

    public static class FreeText {
        public String English;
        public String Indonesian;
    }

    public static class InquiryReason {
        public String English;
        public String Indonesian;
    }
}
