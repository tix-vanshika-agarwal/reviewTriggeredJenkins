package com.tiket.web.page.common.home;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.CoreMatchers.is;

public class InfoPageMWeb<T extends WebDriver> extends InfoPageWeb<T> {

    public InfoPageMWeb(Session<T> session) {
        super(session);
    }
    private static final String STAGING_DWEB_URL = "https://m.gatotkaca.tiket.com/";
    private static final String PREPROD_DWEB_URL = "https://m.preprod.tiket.com/";
    private static final String PROD_DWEB_URL = "https://m.tiket.com/";

    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//button[contains(@class, 'MobileBackButton_back_button')]"));

    @Override
    public void clickBackButton() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.clickWithJavascriptExecutor(BACK_BUTTON);
    }
}
