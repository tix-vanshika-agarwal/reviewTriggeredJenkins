package com.tiket.pojo.utilities;

import java.util.ArrayList;

public class SectionModulePOJO {

    public String titleSection;
    public String subTitleSection;
    public ArrayList<Chips> chipsItem;

    public static class Chips {
        public String titleChips;
        public String buttonText;
        public String productType;
        public ArrayList<Content> contentsItem;
    }

    public static class Content {
        public String clickUrl;
    }
}
