package com.tiket.pojo.ttd;

import java.math.BigInteger;

public class TtdCheckOrderResponse {
    public String id;
    public String coreOrderId;
    public String coreOrderHash;
    public String status;
    public BigInteger totalAmountInCents;
    public BigInteger totalAmount;
    public int scale;
    public String paymentCurrency;
    public float currencyRate;
    public String blockedExpiryDate;
    public String paymentUrl;
}
