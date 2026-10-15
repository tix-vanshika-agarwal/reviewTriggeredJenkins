package com.tiket.pojo.paymentDweb;

import java.util.List;

public class BCAVASnapInquiryResponse {
    public String responseCode;
    public String responseMessage;
    public VirtualAccountData virtualAccountData;

    public static class VirtualAccountData {
        public String inquiryStatus;
        public InquiryReason inquiryReason;
        public String partnerServiceId;
        public String customerNo;
        public String virtualAccountNo;
        public String virtualAccountName;
        public String inquiryRequestId;
        public Amount totalAmount;
        public String subCompany;
        public List<FreeText> freeTexts;
    }

    public static class InquiryReason {
        public String english;
        public String indonesia;
    }

    public static class Amount {
        public String value;
        public String currency;
    }

    public static class FreeText {
        public String english;
        public String indonesia;
    }
}
