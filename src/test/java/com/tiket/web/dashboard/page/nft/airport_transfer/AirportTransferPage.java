package com.tiket.web.dashboard.page.nft.airport_transfer;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AirportTransferPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_INPUT_FIELD = new Locator("SEARCH_INPUT_FIELD", new By.ByXPath("//input[@placeholder='Search']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));

    public AirportTransferPage(Session<T> session) {
        super(session);
    }

    public void goToATPage(String username, String password) {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer");
        }
    }

    public void goToATPageViaUI(String username, String password) {
        driverTools.maximizeWindow();

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member");
        }

        if (!isAirportTransferLoggedIn()) {
            AirportTransferLoginPage<?> atLoginPage = new AirportTransferLoginPage<>(session);
            atLoginPage.loginToAirportTransferPage(username, password);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 2);

            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/airport-transfer");
            }
        }
    }

    public boolean isAirportTransferLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void goToFacilityTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer/master-data/facility");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer/master-data/facility");
        }
    }


    public String searchFacilityByLanguage(String facility, String language) {
        driverTools.waitForElementToAppear(SEARCH_INPUT_FIELD);
        driverTools.sendKeys(SEARCH_INPUT_FIELD, facility);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);

        String index = "";
        if (language.equals("ID")) {
            index = "4";
        } else if (language.equals("EN")) {
            index = "5";
        }

        Locator FACILITY_NAME = getFacilityNameLocator(index);
        driverTools.waitForElementToAppear(FACILITY_NAME);
        return String.valueOf(driverTools.getText(FACILITY_NAME));
    }

    private Locator getFacilityNameLocator(String index) {
        return new Locator("FACILITY_NAME", new By.ByXPath("(//td[@class='ant-table-cell'])[" + index + "]"));
    }
}