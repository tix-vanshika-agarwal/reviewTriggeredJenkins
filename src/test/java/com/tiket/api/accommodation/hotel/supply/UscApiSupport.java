package com.tiket.api.accommodation.hotel.supply;

import com.tiket.model.api.UriHelper;

/** Shared host + headers for the USC endpoints on tix-hotel-supply-config. */
final class UscApiSupport {

    static final String HEADERS_PATH = "src/test/resources/api/accommodation/hotel/supply/UscSupplyConfig_headers.properties";
    static final String BASE_PATH = "/tix-hotel-supply-config/usc/v1";

    private UscApiSupport() {
    }

    static UriHelper uriHelper() {
        return new UriHelper(
                "http://hotel-supply-config.accom-supply-ns.svc.tiket",
                "https://hotel-supply-config-svc.preprod-hotel-cluster.tiket.com");
    }
}
