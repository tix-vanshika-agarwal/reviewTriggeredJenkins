package com.tiket.pojo.bus;

import java.util.ArrayList;

public class BusSearchResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data {
        public ArrayList<Schedules> schedules;
    }

    public static class Schedules {
        public String id;
        public String hash;
    }
}
