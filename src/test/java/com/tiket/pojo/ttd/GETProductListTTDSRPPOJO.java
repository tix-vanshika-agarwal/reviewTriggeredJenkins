package com.tiket.pojo.ttd;

import java.util.List;

public class GETProductListTTDSRPPOJO {
    public List<ProductCard> data;

    public static class ProductCard {
        public int scale;
        public int startingCrossedPrice;
        public int startingFinalPrice;
        public List<Translations> translations;
    }

    public static class Translations {
        public String language;
        public String url;
        public String title;
        public String area;
        public String city;
        public String region;
        public String country;
        public double avgRating;
    }
}
