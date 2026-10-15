package com.tiket.model;

import org.apache.commons.lang3.NotImplementedException;

public enum Environment {
    GK, PROD, PREPROD;

    public static Environment parse(String env) {
        switch (env.toLowerCase()) {
            case "gk" -> {
                return GK;
            }
            case "prod" -> {
                return PROD;
            }
            case "preprod" -> {
                return PREPROD;
            }
            default -> throw new NotImplementedException(String.format("Given environment: %s is not supported", env));
        }
    }
}
