package com.tiket.pojo.discount;

import java.math.BigInteger;

public class DiscountBody {
    public String orderId;
    public String orderHash;
    public String discountCode;
    public String productType;
    public BigInteger totalPrice;
    public String currency;
    public String paymentType;
}
