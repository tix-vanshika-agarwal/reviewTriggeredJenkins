package com.tiket.pojo.utilities;

import java.util.ArrayList;

public class GSLandingResponse {
    public boolean success;
    public String code;
    public String message;
    public ArrayList<Data> data;
    public float serverTime;

    public static class Data {
        public String id;
        public String templateCode;
        public String processType;
        public boolean isAnchor;
        public ArrayList<DynamicContet> dynamicContents;
        public String sectionName;
        public ModuleDetail moduleDetail;
    }

    public static class ModuleDetail {
        public Heading heading;
    }

    public static class Heading{
        public String lang;
        public String title;
        public String subtitle;
    }

    public static class DynamicContet{
        public String buttonText;
        public String contentTitle;
        public String buttonUrl;
        public String image;
    }
}
