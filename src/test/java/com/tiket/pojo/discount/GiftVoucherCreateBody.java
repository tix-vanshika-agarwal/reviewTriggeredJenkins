package com.tiket.pojo.discount;

import java.util.List;
import java.util.Map;

public class GiftVoucherCreateBody {
    public int discountAmount;
    public int discountPercentage;
    public int maximumVoucherAmount;
    public int minimumTransactionAmount;
    public List<String> userEmails;
    public String currency;
    public String description;
    public String giftVoucherAdjustmentCode;
    public String lang;
    public boolean sendNotification;
    public Map<String, String> shortDescriptions;
    public Map<String,List<String>> termsConditions;
    public boolean userLock;
    public String validFromDate;
    public String validUntilDate;
    public String voucherType;
    public int cashbackAmount;
    public int maximumVoucherCashbackAmount;
    public String discountScheme;
    public List<String> validityRestriction;
}