package com.tiket.pojo.payment;

import java.util.List;

public class InstallmentDetailResponse {

    public String code;
    public String message;
    public Object errors;
    public List<InstallmentOption> data;
    public Long serverTime;

    public static class InstallmentOption {
        public int installmentMonth;
        public String currency;
        public Double payMonthly;
        public Double grandTotal;
        public String text;
        public String id;
        public String note;
        public Double chargePercentage;
        public Double charge;
        public Double monthlyCharge;
        public Double serviceCharge;
        public String limitNote;
        public Boolean enable;
    }
}
