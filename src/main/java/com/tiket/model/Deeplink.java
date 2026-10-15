package com.tiket.model;

import java.util.Objects;

public record Deeplink(String name, String gkDeeplink, String preprodDeeplink, String prodDeeplink) {
    public Deeplink {
        Objects.requireNonNull(name);
        Objects.requireNonNull(gkDeeplink);
        Objects.requireNonNull(preprodDeeplink);
        Objects.requireNonNull(prodDeeplink);
    }

    public Deeplink(String name, String gkDeeplink, String prodDeeplink) {
        this(name, gkDeeplink, gkDeeplink, prodDeeplink);
    }
}
