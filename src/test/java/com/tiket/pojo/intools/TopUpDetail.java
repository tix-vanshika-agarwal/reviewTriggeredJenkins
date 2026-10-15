package com.tiket.pojo.intools;

import java.util.List;

public class TopUpDetail {
    public String entity;
    public String uid;
    public String vendorName;
    public String subVendorName;
    public String vendorId;
    public String source;
    public String settlementType;
    public String vendorCurrency;
    public String startDepositBalance;
    public String endDepositBalance;
    public String topUpAmount;
    public String trxpAmount;
    public String typeOfAdjustment;
    public String typeOfTransaction;
    public String description;
    public String orderId;


    public TopUpDetail(List<String> detailsTopUp) {
        if (!detailsTopUp.isEmpty()) {
            this.entity = detailsTopUp.get(1);
            this.uid = detailsTopUp.get(2);
            this.orderId = detailsTopUp.get(3);
            this.vendorName = detailsTopUp.get(7);
            this.subVendorName = detailsTopUp.get(9);
            this.source = detailsTopUp.get(19);
            this.settlementType = detailsTopUp.get(20);
            this.vendorCurrency = detailsTopUp.get(88);
            this.startDepositBalance = detailsTopUp.get(113);
            this.endDepositBalance = detailsTopUp.get(114);
            this.topUpAmount = detailsTopUp.get(116);
            this.trxpAmount = detailsTopUp.get(117);
            this.typeOfAdjustment = detailsTopUp.get(118);
            this.typeOfTransaction = detailsTopUp.get(119);
            this.description = detailsTopUp.get(120);
        } else {
            throw new IllegalArgumentException("Invalid detailsTopUp list: Insufficient elements");
        }
    }

    public TopUpDetail(String[] detailsTopUp,String entity, String vendorId, String vendorCurrency, String settlementType, String vendorName) {
        if (detailsTopUp.length > 0) {
            this.entity = entity;
            this.vendorId = vendorId;
            this.vendorName = vendorName;
            this.subVendorName = detailsTopUp[10];
            this.source = "Manual";
            this.settlementType = settlementType;
            this.vendorCurrency = vendorCurrency;
            this.trxpAmount = detailsTopUp[6];
            this.typeOfAdjustment = detailsTopUp[7];
            this.typeOfTransaction = detailsTopUp[5];
            this.orderId = detailsTopUp[3];
        } else {
            throw new IllegalArgumentException("Invalid detailsTopUp list: Insufficient elements");
        }
    }
}
