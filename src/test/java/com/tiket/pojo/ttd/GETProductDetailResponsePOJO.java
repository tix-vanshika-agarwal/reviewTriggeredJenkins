package com.tiket.pojo.ttd;

import java.util.List;

public class GETProductDetailResponsePOJO {
    public Data data;

    public static class Data {
        public List<Package> packages;
    }

    public static class Package {
        public List<Translation1> translations;
        public RefundPriceRateResult refundPriceRateResult;
        public boolean isRefundable;
        public String code;
    }

    public static class Translation1 {
        public String language;
        public String name;
    }

    public static class RefundPriceRateResult {
        public List<RefundReasonTier> refundReasonTiers;
    }


    public static class RefundReasonTier {
        public List<Tier> tiers;
        public List<Translation2> translations;
    }

    public static class Translation2 {
        public String language;
        public String reasonName;
    }

    public static class Tier {
        public int refundPercentage;
        public String timeUnit;
        public int value;
        public int inDaysValue;
    }
}
