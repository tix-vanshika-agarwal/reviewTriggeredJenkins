package com.tiket.pojo.giftCard;

import java.util.List;

public class GiftCardOrderBody {
    public String cartId;
    public String cartSecret;
    public Customer customer;
    public List<BookingDetail> bookingDetail;

    public static class Customer {
        public String profileId;
        public Detail detail;
    }

    public static class Detail {
        public String areaCode;
        public String emailAddress;
        public String fullName;
        public String phone;
        public String title;
    }

    public static class BookingDetail {
        public String senderName;
        public String schedule;
        public String scheduleDateTime;
        public String scheduleTimeZone;
    }
}
