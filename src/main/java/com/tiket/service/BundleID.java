package com.tiket.service;

import com.tiket.testbase.Configs;

public class BundleID {

    private static final String BUNDLE_ID_PROD = "com.mobile.ios.Tiket";
    private static final String BUNDLE_ID_GK = "com.mobile.ios.Tiket.dev";
    private static final String BUNDLE_ID_PREPROD = "com.mobile.ios.Tiket.preprod";

    public static String getID() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> BUNDLE_ID_GK;
            case PREPROD -> BUNDLE_ID_PREPROD;
            case PROD -> BUNDLE_ID_PROD;
        };
    }
}
