package com.tiket.pojo.accommodation.homes;

import java.util.ArrayList;

public class HomesData {
    public String title;
    public String subTitle;
    public ArrayList<ContentData> contentDataList;
    public ArrayList<String> modules;
    public String landingPageId;
    public String templateCode;
    public String subsidyLabel;
    public int index;
    public ArrayList<String> contentListTitle;
    public boolean showButton;
    public String buttonText;
    public String redirectTarget;
    public ArrayList<ListOfTitleAndSubTitle> listOfTitleAndSubTitles;
    public ContentData contentData;
    public ContentListData contentListData;
    public ArrayList<com.tiket.pojo.accommodation.homes.HomesNHAResponse.List> list;
    public static class ContentData{

        public String location;
        public String title;
        public String buttonText;
        public String redirectTarget;
        public String redirectLocation;
        public com.tiket.pojo.accommodation.homes.HomesNHAResponse.Redirect redirect;
    }

    public static class ListOfTitleAndSubTitle{
        public String title;
        public String subTitle;
    }

    public static class ContentListData{
        public String name;
        public String location;
        public String buttonText;
        public String redirectTarget;
        public String locationInsideListRedirect;
        public String locationTypeInsideListRedirect;
    }
}
