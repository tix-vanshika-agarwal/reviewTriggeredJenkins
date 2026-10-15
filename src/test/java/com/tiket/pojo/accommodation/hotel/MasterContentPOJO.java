package com.tiket.pojo.accommodation.hotel;

public class MasterContentPOJO {
    public MasterContentData contentList ;
    public String accessToken;
    public String userAgent;


    public static class MasterContentData {
        public String id;
        public String topTitle;
        public String title;
        public String subTitle;
        public String icon;
        public String templateCode;
        public boolean loginOnly;
        public String type;
    }

}
