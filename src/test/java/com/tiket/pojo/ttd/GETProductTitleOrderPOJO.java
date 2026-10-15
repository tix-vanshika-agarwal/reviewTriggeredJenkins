package com.tiket.pojo.ttd;

import java.util.List;

public class GETProductTitleOrderPOJO {
    public Data data;

    public static class Data {
        public List<ProductList> popularEvents;
        public List<ProductList> popularAttractions;
        public List<ProductList> recentlyAdded;
    }

    public static class ProductList {
        public String id;
        public List<Translations> translations;

        public static class Translations {
            public String language;
            public String title;
        }
    }
}
