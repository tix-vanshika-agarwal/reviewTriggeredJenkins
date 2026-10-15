package com.tiket.pojo.giftCard;

public class GiftCardIssuanceResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public String bookingStatus;
    }
}
