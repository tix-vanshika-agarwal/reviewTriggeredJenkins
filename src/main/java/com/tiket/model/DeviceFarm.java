package com.tiket.model;

import com.tiket.capabilities.CapabilitiesHandler;

public enum DeviceFarm {
    BROWSER_STACK, LAMBDA_TEST, LOCAL;

    public static DeviceFarm parse(String deviceFarm) {
        if(CapabilitiesHandler.isRunningOnDeviceFarm()) {
            if ("browserstack".equalsIgnoreCase(deviceFarm)) {
                return BROWSER_STACK;
            }
            return LAMBDA_TEST;
        } else {
            return LOCAL;
        }
    }
}
