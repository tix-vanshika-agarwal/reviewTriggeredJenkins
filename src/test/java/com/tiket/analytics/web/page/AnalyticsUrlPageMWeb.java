package com.tiket.analytics.web.page;

import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;

public class AnalyticsUrlPageMWeb <T extends WebDriver> extends AnalyticsUrlPageWeb<T> {
    public AnalyticsUrlPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void openHomePageEn() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driverTools.get("https://m.gatotkaca.tiket.com/");
            }
            case PROD -> {
                driverTools.get("https://m.tiket.com/");
            }
            case PREPROD -> {
                driverTools.get("https://m.preprod.tiket.com/");
            }
        }
    }

    @Override
    public void openUrl(String path) {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driverTools.get("https://m.gatotkaca.tiket.com/" + path);
            }
            case PROD -> {
                driverTools.get("https://m.tiket.com/" + path);
            }
            case PREPROD -> {
                driverTools.get("https://m.preprod.tiket.com/" + path);
            }
        }
    }
}