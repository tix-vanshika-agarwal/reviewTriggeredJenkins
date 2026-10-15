package com.tiket.pojo.train;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TrainOrderResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public String state;
        public Journey journeys;
        public Order order;
        public OrderPrice price;
    }

    public static class Order{
        public long id;
        public String hash;
    }

    public static class OrderIdentifier{
        String id;
        String hash;
        Object productOrderId;
        String state;
        OrderIdentifier identifier;
    }

    public static class Journey{
        public Depart depart;
    }

    public static class Depart{
        public ArrayList<Schedule> schedules;
    }

    public static class Schedule{
        public DepartStation departureStation;
        public ArrivalStation arrivalStation;
        public String trainName;
        public String departureTime;
        public String arrivalTime;
    }

    public static class DepartStation{
        public String code;
        public String name;
    }

    public static class ArrivalStation{
        public String code;
        public String name;
    }

    public static class OrderPrice{
        public BigDecimal olderPrice;
        public BigDecimal newPrice;
        public boolean isPriceChanged;
    }
}
