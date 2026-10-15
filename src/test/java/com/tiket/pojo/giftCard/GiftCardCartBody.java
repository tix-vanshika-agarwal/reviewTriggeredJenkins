package com.tiket.pojo.giftCard;

import java.util.List;

public class GiftCardCartBody {
    public List<Passenger> passengers;
    public List<Inventory> inventories;

    public static class Passenger {
        public int paxId;
        public String profileId;
        public Detail detail;
    }

    public static class Detail {
        public String fullName;
        public String areaCode;
        public String phone;
        public String emailAddress;
        public String title;
    }

    public static class Inventory {
        public String searchKey;
        public String inventoryId;
        public String vertical;
        public BookingCount bookingCount;
        public List<BookingItem> bookingItems;
    }

    public static class BookingCount {
        public int recipientCount;
    }

    public static class BookingItem {
        public int paxId;
        public int voucherAmount;
    }
}