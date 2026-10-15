package com.tiket.analytics.web.page;

import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;

public class AnalyticsUrlPageDWeb <T extends WebDriver> extends AnalyticsUrlPageWeb<T>  {
    public AnalyticsUrlPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void openHomePageEn() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driverTools.get("https://en.gatotkaca.tiket.com/");
            }
            case PROD -> {
                driverTools.get("https://en.tiket.com/");
            }
            case PREPROD -> {
                driverTools.get("https://en.preprod.tiket.com/");
            }
        }
    }

    @Override
    public void openUrl(String path) {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driverTools.get("https://gatotkaca.tiket.com/" + path);
            }
            case PROD -> {
                driverTools.get("https://tiket.com/" + path);
            }
            case PREPROD -> {
                driverTools.get("https://preprod.tiket.com/" + path);
            }
        }
    }
}