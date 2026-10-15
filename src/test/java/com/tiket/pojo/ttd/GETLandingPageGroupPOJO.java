package com.tiket.pojo.ttd;

import java.util.List;

public class GETLandingPageGroupPOJO {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public Page page;
        public List<Keyword> keywords;
        public Object destination;
    }

    public static class Page {
        public String code;
        public BackgroundImage backgroundImage;
        public List<MenuGroup> menuGroups;
        public Object iconImage;
    }

    public static class BackgroundImage {
        public Object urlSource;
        public String urlSmall;
        public String urlMedium;
        public String urlLarge;
        public List<Translation> translations;
        public Object showInGallery;
    }

    public static class MenuGroup {
        public int priority;
        public List<Translation> translations;
        public ShowAll showAll;
        public List<Menu> menus;
        public IconImage iconImage;
    }

    public static class ShowAll {
        public String bottomsheet;
        public List<Translation> translations;
        public IconImage iconImage;
    }

    public static class Menu {
        public List<Translation> translations;
        public String url;
        public IconImage iconImage;
        public Label label;
    }

    public static class IconImage {
        public Object urlSource;
        public String urlSmall;
        public String urlMedium;
        public String urlLarge;
        public List<Translation> translations;
        public Object showInGallery;
    }

    public static class Label {
        public List<Translation> translations;
    }

    public static class Translation {
        public String language;
        public String alt;
        public String description;
        public String name;
        public String contentLabel;
    }

    public static class Keyword {
        public String id;
        public String code;
        public List<Translation> translations;
    }
}
