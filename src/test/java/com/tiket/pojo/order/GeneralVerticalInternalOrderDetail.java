package com.tiket.pojo.order;

import java.math.BigDecimal;
import java.util.ArrayList;

public class GeneralVerticalInternalOrderDetail {

    public String code;
    public Data data;

    public static class Data {
        public String orderId;
        public String orderHash;
        public String orderStatus;
        public BigDecimal price;
        public ArrayList<OrderDetail> orderDetails;
        public String userLang;
        public ArrayList<Payment> payment;
    }

    public static class OrderDetail{
        public String orderName;
        public String orderNameDetail;
        public String type;
        public Number sellingPrice;
        public Vertical vertical;
        public String orderDetailId;
        public String bookingCode;
    }

    public static class Vertical{
        public String orderDetailId;
        public String departureDate;
        public String tripType;
        public String bookingCheckinDate;
        public String bookingCheckoutDate;
        // --- Added for flight order support ---
        public String flightDate; // e.g. 2025-06-22
        public String departureTime; // e.g. 2025-06-22 10:00:00
        public String arrivalTime; // e.g. 2025-06-22 11:55:00
        public String airlinesName;
        public ArrayList<Tracker> trackers; // for extracting startDate and other info
        // ...add more fields as needed for other verticals...
    }

    public static class Tracker {
        public String key;
        public String value;
    }

    public static class Payment{
        public String paymentCurrency;
        public String orderId;
        public String paymentId;
        public Number paymentAmount;
        public String paymentSource;
        public String paymentMethod;

        public Double getPaymentAmounAsDouble(){
            return ((Number) paymentAmount).doubleValue();
        }
    }
}
