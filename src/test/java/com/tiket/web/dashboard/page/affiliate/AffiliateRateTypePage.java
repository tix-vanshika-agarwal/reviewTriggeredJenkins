package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;


public class AffiliateRateTypePage<T extends WebDriver> extends BasePage<T> {
    public AffiliateRateTypePage(Session<T> session) {
        super(session);
    }

    private static final Locator API_PRODUCTS = new Locator("API_PRODUCTS", By.xpath("//p[text()='API Products']"));
    private static final Locator TTD_SECTION = new Locator("TTD_SECTION", By.xpath("(//div[text()='TTD'])[1]"));
    private static final Locator SETTINGS = new Locator("SETTINGS", By.xpath("(//input[@placeholder='Settings'])[1]"));
    private static final Locator GENERAL_CONFIG = new Locator("GENERAL_CONFIG", By.xpath("//span[text()='General Config']"));
//    private static final Locator RATE_TYPE = new Locator("RATE_TYPE", By.xpath("//span[text()='Edit Rate Type']"));
    private static final Locator NET_RATE = new Locator("NET_RATE", By.xpath("//span[text()='Net Rate']"));
    private static final Locator SELL_RATE = new Locator("SELL_RATE", By.xpath("//span[text()='Sell Rate']"));
    private static final Locator COMMISSION_VALUE = new Locator("COMMISSION_VALUE", By.xpath("//input[@placeholder='Commission Value']"));
    private static final Locator SAVE = new Locator("SAVE", By.xpath("//button[text()='Save']"));
    private static final Locator SAVE_RATE_TYPE = new Locator("SAVE_RATE_TYPE", By.xpath("//div[text()='Rate Type successfully saved']"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_type_success')]"));

    public void goToEditRateType() {
        driverTools.waitForElementToAppear(API_PRODUCTS, 5);
        driverTools.click(API_PRODUCTS);
        driverTools.waitForElementToAppear(TTD_SECTION, 5);
        driverTools.click(TTD_SECTION);
        driverTools.waitForElementToAppear(SETTINGS, 5);
        driverTools.click(SETTINGS);
        driverTools.waitForElementToAppear(GENERAL_CONFIG, 5);
        driverTools.click(GENERAL_CONFIG);
//        driverTools.waitForElementToAppear(RATE_TYPE, 5);
//        driverTools.click(RATE_TYPE);
    }

    public void selectNetRate() {
        driverTools.waitForElementToAppear(NET_RATE, 5);
        driverTools.click(NET_RATE);
    }

    public void selectSellRate() {
        driverTools.waitForElementToAppear(SELL_RATE, 5);
        driverTools.click(SELL_RATE);
    }

    public void inputCommissionValue(String commission) {
        driverTools.waitForElementToAppear(COMMISSION_VALUE);
        driverTools.clearInput(COMMISSION_VALUE);
        driverTools.sendKeys(COMMISSION_VALUE, commission);
    }

    public void saveEditRateType() {
        driverTools.waitForElementToAppear(SAVE, 5);
        driverTools.click(SAVE);
    }

    public boolean checkGeneralConfigChangesSnackbar(String expectedSnackbarText){
        driverTools.waitForElementToAppear(SNACKBAR, 2).orElseThrow();
        String snackbarText = driverTools.getText(SNACKBAR).orElseThrow();
        return driverTools.isElementPresent(SNACKBAR) && snackbarText.contains(expectedSnackbarText);
    }
}