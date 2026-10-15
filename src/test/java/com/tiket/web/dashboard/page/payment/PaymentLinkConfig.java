package com.tiket.web.dashboard.page.payment;

import java.time.LocalDateTime;

public class PaymentLinkConfig {
    public String clientId;
    public LocalDateTime expiryTime;
    public LocalDateTime bufferExpiryTime;
    public String currency;
    public int maxNumberOfTimes;
    public String productTitleEn;
    public String productDescriptionEn;
    public String productTitleId;
    public String productDescriptionId;
    public String itemNameEn;
    public String itemNameId;
    public long unitPrice;
    public int quantity;
    public String paymentMethod;
    public String paymentGroup;
    public String paymentMethodConsumer;
}
