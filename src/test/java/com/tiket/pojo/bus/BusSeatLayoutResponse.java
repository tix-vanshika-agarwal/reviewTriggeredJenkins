package com.tiket.pojo.bus;

import java.util.ArrayList;

public class BusSeatLayoutResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data {
        public ArrayList<SeatMap> seatMap;
    }

    public static class SeatMap {
        public int floor;
        public int totalRows;
        public int totalColumns;
        public ArrayList<Rows> rows;
    }

    public static class Rows {
        public String name;
        public boolean available;
    }
}
