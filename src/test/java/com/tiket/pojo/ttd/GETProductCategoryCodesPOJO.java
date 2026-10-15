package com.tiket.pojo.ttd;

import java.util.List;

public class GETProductCategoryCodesPOJO {
    public List<ProductCategory> data;

    public static class ProductCategory {
        public String id;
        public String code;
        public List<Translation> translations;
    }

    public static class Translation {
        public String language;
        public String name;
        public String description;
    }
}
