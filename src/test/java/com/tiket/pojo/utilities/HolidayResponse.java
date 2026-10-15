package com.tiket.pojo.utilities;

import java.util.ArrayList;

public class HolidayResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public Object serverTime;

    public static class Data {
        public ArrayList<Content> content;
        public Pageable pageable;
        public int totalElements;
        public int totalPages;
        public boolean last;
        public boolean first;
        public int number;
        public int numberOfElements;
        public int size;
        public boolean empty;
    }

    public static class Content {
        public String id;
        public String date;
        public ArrayList<Description> descriptions;
    }

    public static class Description {
        public String lang;
        public String content;
    }

    public static class Pageable {
        public Sort sort;
        public int pageSize;
        public int pageNumber;
        public int offset;
        public boolean unpaged;
        public boolean paged;
    }

    public static class Sort {
        public boolean empty;
        public boolean unsorted;
        public boolean sorted;
        public String direction;
        public String property;
        public boolean ignoreCase;
        public String nullHandling;
        public boolean ascending;
        public boolean descending;
    }

}
