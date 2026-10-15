package com.tiket.pojo.payment;

import java.util.List;

public class BCAVANotifyApiResponse {
    public String CompanyCode;
    public String CustomerNumber;
    public String RequestID;
    public String PaymentFlagStatus;
    public PaymentFlagReason PaymentFlagReason;
    public String CustomerName;
    public String CurrencyCode;
    public String PaidAmount;
    public String TotalAmount;
    public String TransactionDate;
    public Object DetailBills; // Use appropriate type if structure is known
    public List<FreeText> FreeTexts;
    public String AdditionalData;

    public static class PaymentFlagReason {
        public String Indonesian;
        public String English;
    }

    public static class FreeText {
        public String Indonesian;
        public String English;
    }
}
