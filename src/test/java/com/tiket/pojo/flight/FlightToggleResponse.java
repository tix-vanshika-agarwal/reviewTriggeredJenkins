package com.tiket.pojo.flight;

public class FlightToggleResponse {
    public String code;
    public String message;
    public Data data;
    public Object errors;

    public static class Data{
        public String status; // "enable" / "disable"
    }
}
