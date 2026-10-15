package com.tiket.service;

import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.testbase.Configs;

public class RunID {

    private static final String ANDROID_RUN_ID_PROD = Configs.PROPERTIES_DATA.get("testrail.run.prod.android");
    private static final String IOS_RUN_ID_PROD = Configs.PROPERTIES_DATA.get("testrail.run.prod.ios");
    private static final String ANDROID_RUN_ID_PREPROD = Configs.PROPERTIES_DATA.get("testrail.run.preprod.android");
    private static final String IOS_RUN_ID_GK = Configs.PROPERTIES_DATA.get("testrail.run.gk.ios");
    private static final String IOS_RUN_ID_PREPROD = Configs.PROPERTIES_DATA.get("testrail.run.preprod.ios");
    private static final String ANDROID_RUN_ID_GK = Configs.PROPERTIES_DATA.get("testrail.run.gk.android");
    private static final String WEB_RUN_ID_PROD = Configs.PROPERTIES_DATA.get("testrail.run.prod.web");
    private static final String WEB_RUN_ID_GK = Configs.PROPERTIES_DATA.get("testrail.run.gk.web");
    private static final String WEB_RUN_ID_PREPROD = Configs.PROPERTIES_DATA.get("testrail.run.preprod.web");

    public static String getID() {
        if (Configs.ENVIRONMENT == Environment.GK && Configs.PLATFORM == Platform.IOS) {
            return IOS_RUN_ID_GK;
        } else if (Configs.ENVIRONMENT == Environment.GK && Configs.PLATFORM == Platform.ANDROID) {
            return ANDROID_RUN_ID_GK;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD && Configs.PLATFORM == Platform.IOS) {
            return IOS_RUN_ID_PREPROD;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD && Configs.PLATFORM == Platform.ANDROID) {
            return ANDROID_RUN_ID_PREPROD;
        } else if (Configs.ENVIRONMENT == Environment.PROD && Configs.PLATFORM == Platform.IOS) {
            return IOS_RUN_ID_PROD;
        } else if (Configs.ENVIRONMENT == Environment.PROD && Configs.PLATFORM == Platform.ANDROID){
            return ANDROID_RUN_ID_PROD;
        } else if (Configs.ENVIRONMENT == Environment.PROD && (Configs.PLATFORM == Platform.DWEB || Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB)) {
            return WEB_RUN_ID_PROD;
        } else if (Configs.ENVIRONMENT == Environment.GK && (Configs.PLATFORM == Platform.DWEB || Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB)) {
            return WEB_RUN_ID_GK;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD && (Configs.PLATFORM == Platform.DWEB || Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB)) {
            return WEB_RUN_ID_PREPROD;
        }
        return "Please check your testdata for testrailid";
    }
}