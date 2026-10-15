package com.tiket.pojo.ferry;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AddToCartFerryResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data{
        public String cartId;
        public String secret;
        public PriceDetail priceDetail;
        public ArrayList<Schedule> schedules;
    }

    public static class PriceDetail{
        public BigDecimal totalPrice;
    }

    public static class Schedule{
        public String tripType;
        public String id;
        public String departureDate;
        public String departureTimeStart;
        public String departureTimeEnd;
        public String timeZone;
        public VehicleType vehicleType;
        public Object ship;
    }

    public static class VehicleType{
        public boolean isLicensePlateRequired;
        public Object categories;
        public String id;
    }
}
