package com.tiket.pojo.intools;

import java.util.List;

public class B2CDailyDetail {
    public List<Data> data;

    public static class Data {
        public String entityId;
        public List<Detail> detail;
    }

    public static class Detail {
        public String vendorId;
        public String vendorName;
        public String source;
        public String settlementType;
        public Double startDepositBalance;
        public Double endDepositBalance;
        public Double topUpAmount;
        public String typeOfAdjustment;
        public String typeOfTransaction;
        public String description;

    }
}