package com.tiket.pojo.utilities;

import java.util.ArrayList;

public class HomePageVerticalDataPojo {
    public Data data;

    public static class Data {
        public VerticalIcons verticalIcons;
    }

    public static class VerticalIcons{
        public String sequenceId;
        public ArrayList<Vertical> verticals;
        public Object primaryId;
    }

    public static class Vertical{
        public String id;
        public String code;
        public String name;
        public String altText;
        public Label label;
        public String typeAction;
        public String iconUrl;
        public String clickUrl;
        public boolean isPromptUpdate;
        public String trackerCode;
        public String currency;
    }

    public static class Label{
        public String color;
        public String value;
    }
}
