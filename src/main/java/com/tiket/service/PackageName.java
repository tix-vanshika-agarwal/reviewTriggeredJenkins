package com.tiket.service;

import com.tiket.testbase.Configs;

public class PackageName {

    private static final String PACKAGE_NAME_PROD = "com.tiket.gits";
    private static final String PACKAGE_NAME_GK = "com.tiket.gits.debug";
    private static final String PACKAGE_NAME_PREPROD = "com.tiket.gits.preprod";

    public static String get() {
        return switch (Configs.ENVIRONMENT) {
            case PREPROD -> PACKAGE_NAME_PREPROD;
            case GK -> PACKAGE_NAME_GK;
            case PROD -> PACKAGE_NAME_PROD;
        };
    }
}