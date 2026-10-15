package com.tiket.pojo.giftCard;

import java.util.List;

public class GiftCardOrderDetailsResponse {
    public List<Passenger> passengers;
    public List<Inventory> inventories;
    public String cartId;
    public String bookingStatus;
    public String currency;
    public String cartSecret;

    public static class Passenger {
        public Object paxType;
        public int profileId;
        public String paxId;
        public Object seatId;
        public Detail detail;
    }

    public static class Detail {
        public String areaCode;
        public String emailAddress;
        public String phone;
        public String fullName;
        public String title;
    }

    public static class Inventory {
        public List<BookingItem> bookingItems;
        public Object orderItemId;
        public Object vendorData;
        public Object vertical;
        public Object bookingExpiryDate;
        public Object bookingId;
        public Object bookingType;
        public Object bookingPrice;
        public String id;
        public TiketData tiketData;
        public Object bookingCount;
        public BookingData bookingData;
        public Object inventoryPrice;
        public String status;
    }

    public static class BookingItem {
        public String redeemCode;
        public String voucherMessage;
        public int voucherAmount;
        public String redeemCodeId;
        public int paxId;
        public String maskedRedeemCode;
    }

    public static class TiketData {
        public String occasionImageUrl;
        public String cardName;
        public String cardId;
        public String occasionId;
        public String cardImageUrl;
        public String occasionName;
        public String messageTemplate;
    }

    public static class BookingData {
        public String schedule;
        public String senderName;
        public String scheduleDateTime;
        public String giftCardValidUntil;
        public String scheduleTimeZone;
        public String tncVersion;
    }
}
