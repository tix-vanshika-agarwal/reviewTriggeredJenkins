package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AffiliateMiniAppsPageWeb<T extends WebDriver> extends WebPage<T> {
    public AffiliateMiniAppsPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AffiliateMiniAppsPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AffiliateMiniAppsPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AffiliateMiniAppsPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator PARTNER_SEARCH_INPUT = new Locator("PARTNER_SEARCH_INPUT", By.xpath("//input[@id='partner-idpartner-name']"));
    private static final Locator ADD_NEW_BUTTON = new Locator("ADD_NEW_BUTTON", By.xpath("//button[text()='Add New']"));
    private static final Locator PARTNER_SEARCH_BUTTON = new Locator("PARTNER_SEARCH_BUTTON", By.xpath("//button[text()='Search']"));
    private static final Locator SETTINGS_SELECTION = new Locator("SETTINGS_SELECTION", By.xpath("//span[contains(@class, 'select-none')]"));
    private static final Locator STATUS_TOGGLE = new Locator("STATUS_TOGGLE", By.xpath("(//input[@type='checkbox'])[1]"));
    private static final Locator PROMO_BANNER_STATUS_TOGGLE = new Locator("PROMO_BANNER_STATUS_TOGGLE", By.xpath("(//input[@type='checkbox'])[2]"));
    private static final Locator CHANNEL_ID_INPUT = new Locator("CHANNEL_ID_INPUT", By.xpath("//input[@name='channelId']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@type='submit']"));
    private static final Locator SAVE_COMMISSION_BUTTON = new Locator("SAVE_COMMISSION_BUTTON", By.xpath("//button[text()='Save']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content') and contains(text(), 'Successfully updated content!')]"));
    private static final Locator SELECT_PARTNER_DROPDOWN = new Locator("SELECT_PARTNER_DROPDOWN", By.xpath("//div[contains(@class, 'container') and .//*[text()='Partner Name or ID']]"));
    private static final Locator SELECT_PARTNER_SELECTIONS = new Locator("SELECT_PARTNER_SELECTIONS", By.xpath("//div[@role='option']"));
    private static final Locator SELECT_PARTNER_CREATE_BUTTON = new Locator("SELECT_PARTNER_CREATE_BUTTON", By.xpath("//button[text()='Create']"));


    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> driver.get("https://gatotkaca.tiket.com/admin/affiliate/miniapps");
            case PREPROD -> driver.get("https://preprod.tiket.com/admin/affiliate/miniapps");
            case PROD -> driver.get("https://www.tiket.com/admin/affiliate/miniapps");
        }
        driverTools.dummyWait(10);
        driverTools.refreshWebpage();
    }

    public void searchMiniApps(String partner) {
        driverTools.waitForElementToAppear(PARTNER_SEARCH_INPUT);
        driverTools.click(PARTNER_SEARCH_INPUT);
        driverTools.clearInput(PARTNER_SEARCH_INPUT);
        driverTools.sendKeys(PARTNER_SEARCH_INPUT, partner);
        driverTools.clickWithJavascriptExecutor(PARTNER_SEARCH_BUTTON);
        driverTools.dummyWait(5);
    }

    private Locator getCardContainerForPartnerId(String partnerId) {
        return new Locator("CARD_CONTAINER_" + partnerId, By.xpath("//div[contains(@class, 'card_container') and .//span[contains(., '" + partnerId +"')]]"));
    }

    public boolean checkPartnerIdExist(String partnerId) {
        return driverTools.waitForElementToAppear(getCardContainerForPartnerId(partnerId)).isPresent();
    }

    private void openMenuDetailForMiniApps(String app) {
        Locator accordionLoc = new Locator("ACCORDION_" + app, By.xpath("//div[contains(@class, 'card_container') and .//span[contains(., '" + app +"')]]//div[@role='button']"));
        WebElement accordionElement = driverTools.waitForElementToAppear(accordionLoc).orElseThrow();
        driverTools.click(accordionElement);
    }

    private void selectSettingsOption(String option) {
        List<WebElement> elements = driverTools.waitForElementsToAppear(SETTINGS_SELECTION).orElseThrow();
        driverTools.scrollElementToCenter(elements.get(0));
        for (WebElement element: elements) {
            if (driverTools.getText(element).equalsIgnoreCase(option)) {
                driverTools.click(element);
                break;
            }
        }
    }

    public void selectSettings(String app, String settingOption) {
        openMenuDetailForMiniApps(app);

        Locator dropdownLoc = new Locator("DROPDOWN_" + app, By.xpath("//div[contains(@class, 'card_container') and .//span[contains(., '" + app +"')]]//input"));
        WebElement dropdownElement = driverTools.waitForElementToAppear(dropdownLoc).orElseThrow();
        driverTools.click(dropdownElement);
        selectSettingsOption(settingOption);
        driverTools.dummyWait(3);
    }

    public void toggleContentStatus() {
        WebElement element = driverTools.waitForElementToPresence(STATUS_TOGGLE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
    }

    public String getValueFromChannelIdInput() {
        WebElement e = driverTools.waitForElementToPresence(CHANNEL_ID_INPUT).orElseThrow();
        return e.getAttribute("value");
    }

    public void insertChannelId(String value) {
        WebElement e = driverTools.waitForElementToPresence(CHANNEL_ID_INPUT).orElseThrow();
        driverTools.clearInput(e);
        driverTools.sendKeys(e, value);
    }

    public void clickSave() {
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void clickSaveCommission() {
        driverTools.clickWithJavascriptExecutor(SAVE_COMMISSION_BUTTON);
    }

    public void waitAfterSave() {
        driverTools.dummyWait(5);
    }

    public boolean isSuccessSnackbarAppear() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR).isPresent();
    }

    public void clickAddNewButton() {
        WebElement element = driverTools.waitForElementToAppear(ADD_NEW_BUTTON).orElseThrow();
        driverTools.click(element);
        driverTools.dummyWait(3);
    }

    public String selectPartner() {
        WebElement e = driverTools.waitForElementToPresence(SELECT_PARTNER_DROPDOWN).orElseThrow();
        driverTools.clickElementByCoordinateDWeb(e);

        List<WebElement> partners = driverTools.waitForElementsToAppear(SELECT_PARTNER_SELECTIONS).orElseThrow();
        WebElement partner = partners.get(0);
        String selectedPartner = driverTools.getText(partner);
        driverTools.click(partner);

        driverTools.click(SELECT_PARTNER_CREATE_BUTTON);
        return getThePartnerId(selectedPartner);
    }

    public void togglePromoBannerStatus() {
        WebElement element = driverTools.waitForElementToPresence(PROMO_BANNER_STATUS_TOGGLE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
    }

    private String getThePartnerId(String input) {
        int start = input.indexOf("(") + 1;
        int end = input.indexOf(")");

        if (start > 0 && end > start) {
            return input.substring(start, end);
        }
        return null;
    }
}
