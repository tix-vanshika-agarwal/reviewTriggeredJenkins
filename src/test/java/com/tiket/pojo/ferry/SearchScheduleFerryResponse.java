package com.tiket.pojo.ferry;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SearchScheduleFerryResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data{
        public Configurations configurations;
        public ArrayList<Schedule> schedules;
    }

    public static class Configurations{
        public Object quotaLeftLabels;
    }

    public static class Schedule{
        public int availableQuota;
        public String id;
        public Price price;
        public String shipId;
        public String vendord;
        public String departureDateTimeEnd;
    }

    public static class Price {
        public String currency;
        public BigDecimal finalPrice;
        public long originalPrice;
        public PassengerPrice passenger;
        public PriceDetail details;
    }

    public static class PassengerPrice{
        public long adult;
        public long child;
        public long infant;
        public long elderly;
    }

    public static class PriceDetail{
        public long tax;
    }
}
