package com.tiket.model;

import org.apache.commons.lang3.NotImplementedException;

public enum PriceType {
    DECIMAL, THOUSAND;

    public static PriceType parse(String priceType) {
        switch (priceType.toLowerCase()) {
            case "thousand" -> {
                return THOUSAND;
            }
            case "decimal" -> {
                return DECIMAL;
            }
            default -> throw new NotImplementedException("Given priceType: %s is not supported", priceType);
        }
    }
}
