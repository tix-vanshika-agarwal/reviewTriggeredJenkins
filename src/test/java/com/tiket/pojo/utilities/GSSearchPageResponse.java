package com.tiket.pojo.utilities;

import java.util.ArrayList;

public class GSSearchPageResponse {
    public boolean success;
    public String code;
    public String message;
    public ArrayList<Datum> data;

    public static class Datum{
        public int itemPosition;
        public Object sectionPosition;
        public String type;
        public ArrayList<Content> contents;
    }

    public static class Content{
        public String id;
        public String title;
        public String icon;
        public String subtitle;
    }
}
