package com.tiket.pojo.train;

import java.util.ArrayList;

public class TrainSearchResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public Journeys departJourneys;
    }

    public static class Journeys{
        public int count;
        public ArrayList<AvailableJourneys> journeys;
    }

    public static class AvailableJourneys{
        public String id;
    }
}
