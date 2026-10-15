package com.tiket.pojo.utilities;

import java.util.ArrayList;

public class PromoListResponse {
    public Data data;

    public class Data {
        public ArrayList<Content> contents;
    }

    public class Content {
        public String slug;
    }
}
