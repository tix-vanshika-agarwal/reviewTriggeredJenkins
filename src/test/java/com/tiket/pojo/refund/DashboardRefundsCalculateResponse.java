package com.tiket.pojo.refund;

import java.util.List;

public class DashboardRefundsCalculateResponse {
    public List<RefundMethods> refundMethods;
    public List<GroupedRefundMethods> groupedRefundMethods;
    public List<AmountDetails> amountDetails;
    public List<GenericRefundMethodComponent> genericRefundMethodComponent;

    public static class RefundMethods {
        public String methodKey;
        public MerchantDetails merchantDetails;
    }

    public static class GroupedRefundMethods {
        public String methodKey;
        public MerchantDetails merchantDetails;
    }

    public static class AmountDetails {
        public String amountName;
        public String amountLabel;
        public int amount;
        public String currency;
    }

    public static class GenericRefundMethodComponent {
        public String methodKey;
        public List<ContentsCS> contentsCS;
    }

    public static class MerchantDetails {
        public String merchantName;
    }

    public static class ContentsCS {
        public String label;
        public String value;
    }
}
