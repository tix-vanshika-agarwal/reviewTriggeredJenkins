package com.tiket.pojo.train;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TrainCartBody {
    public PassengerCount passengerCount;
    public String productType;
    public Journey selectedJourneys;

    public static class PassengerCount{
        public int adult;
        public int infant;
    }

    public static class Journey{
        public Depart depart;
        @SerializedName("return")
        public Depart returnJourney;
    }

    public static class Depart{
        public ArrayList<Schedule> schedules;
    }

    public static class Schedule{
        public String id;
        public String originCode;
        public String destinationCode;
        public String departDate;
        public String subClass;
        public String trainNumber;
        public String wagonClass;
    }
}
