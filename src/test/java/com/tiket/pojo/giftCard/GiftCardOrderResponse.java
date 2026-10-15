package com.tiket.pojo.giftCard;

public class GiftCardOrderResponse {
    public String cartId;
    public String bookingStatus;
    public String cartSecret;
    public Order order;

    public static class Order {
        public String paymentExpired;
        public String orderId;
        public String paymentUrl;
        public String orderHash;
    }
}
