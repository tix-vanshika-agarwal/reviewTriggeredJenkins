package com.tiket.pojo.train;

public class TrainCartResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public String id;
        public String secret;
        public String state;
        public Object journeys;
    }
}
