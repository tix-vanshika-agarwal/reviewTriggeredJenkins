package com.tiket.pojo.ttd;

import java.util.List;

public class GETSearchSuggestionResultResponsePOJO {
    public Data data;

    public static class Data {
        public List<Destination> destinations;
        public List<Product> products;
    }

    public static class Destination {
        public String suggestionType;
        public String code;
        public List<Translation> translations;
    }

    public static class Translation {
        public String language;
        public String title;
        public String url;
        public String area;
        public String city;
        public String region;
        public String country;

    }

    public static class Product {
        public String id;
        public String suggestionType;
        public String primaryCategory;
        public List<Translation> translations;
    }
}
