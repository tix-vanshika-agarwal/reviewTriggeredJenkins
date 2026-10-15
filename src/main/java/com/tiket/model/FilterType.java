package com.tiket.model;

import org.apache.commons.lang3.NotImplementedException;

public enum FilterType {
    SORT, CATEGORY, PROMO, FEATURES, DATE, PRICE;

    public static FilterType parse(String filterType) {
        switch (filterType.toLowerCase()) {
            case "sort" -> {
                return SORT;
            }
            case "category" -> {
                return CATEGORY;
            }
            case "promo" -> {
                return PROMO;
            }
            case "features" -> {
                return FEATURES;
            }
            case "date" -> {
                return DATE;
            }
            case "price" -> {
                return PRICE;
            }
            default -> throw new NotImplementedException("Given filterType: %s is not supported", filterType);
        }
    }
}
