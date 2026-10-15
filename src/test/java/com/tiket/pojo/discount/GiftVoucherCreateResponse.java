package com.tiket.pojo.discount;

import java.util.List;

public class GiftVoucherCreateResponse {

    public Data data;

    public static class Data {
        public String giftVoucherCode;
        public int discountAmount;
        public int cashbackAmount;
        public int maximumVoucherCashbackAmount;
        public String discountScheme;
        public List<String> validityRestriction;
        public String giftVoucherAdjustmentCode;
    }
}