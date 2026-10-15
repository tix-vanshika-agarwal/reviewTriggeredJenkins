package com.tiket.pojo.bus;

public class BusSelectScheduleResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data {
        public String scheduleId;
        public String hash;
    }
}
